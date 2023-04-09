package com.example.weatherforecast.ui.alert

import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.Model.*
import com.example.weatherforecast.Network.RemoteSource
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar
import java.util.concurrent.TimeUnit

class PeriodicWorkManager(private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    val repository = Repository.getInstance(
        RemoteSource.getINSTANCE(), LocalSource(context)
    )

    override suspend fun doWork(): Result {
        if (!isStopped) {
            val id = inputData.getLong("id", -1)
            getData(id.toInt())
        }
        return Result.success()
    }

    private suspend fun getData(id: Int) {
        lateinit var current: Welcome
        val alert = repository.getAlert(id)
Log.i("tag",alert.lat+" hadia")
        repository.getAllWeatherData(alert.lat, alert.lon).collectLatest {
            current = it.body()!!

        }
        if (checkTimeLimit(alert)) {
            val delay: Long = getDelay(alert)
            if (current.alerts.isNullOrEmpty()) {
                current.current?.weather?.get(0)?.let {
                    setOneTimeWorkManager(
                        delay, alert.id, it.description
                    )
                }
            } else {
                setOneTimeWorkManager(delay, alert.id, current.alerts!!.get(0).event)
            }
        } else {
            repository.deleteAlert(alert)
            WorkManager.getInstance().cancelAllWorkByTag("$id")
        }
    }

    private fun setOneTimeWorkManager(delay: Long, id: Int, description: String) {
        val data = Data.Builder()
        data.putString("description", description)
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(
            OneTimeWorkManager::class.java,
        )
            .setInitialDelay(delay, TimeUnit.SECONDS)
            .setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "$id",
            ExistingWorkPolicy.REPLACE,
            oneTimeWorkRequest
        )


    }


    private fun getDelay(alert: AlertModel): Long {
        val hour =
            TimeUnit.HOURS.toSeconds(Calendar.getInstance().get(Calendar.HOUR_OF_DAY).toLong())
        val minute =
            TimeUnit.MINUTES.toSeconds(Calendar.getInstance().get(Calendar.MINUTE).toLong())
        return alert.startTime!! - (hour + minute) - (2 * 3600L)

    }

    private fun checkTimeLimit(alert: AlertModel): Boolean {
        val year = Calendar.getInstance().get(Calendar.YEAR)
        val month = Calendar.getInstance().get(Calendar.MONTH)
        val day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val date = "$day/${month + 1}/$year"
        val dayNow = dateToLong(date)

        return dayNow >= alert.startDate!! && dayNow <= alert.endDate!!
    }


}
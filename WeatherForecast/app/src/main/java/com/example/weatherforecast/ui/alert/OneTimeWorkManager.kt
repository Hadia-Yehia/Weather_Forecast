package com.example.weatherforecast.ui.alert

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class OneTimeWorkManager (private val context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters){
    override suspend fun doWork(): Result {
        val description=inputData.getString("description")!!
        startAlertService(description)
        return Result.success()
    }
    private fun startAlertService(s: String) {

        val intent= Intent(applicationContext,AlertService::class.java)
        intent.putExtra("description",s)
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            ContextCompat.startForegroundService(applicationContext,intent)
        }else{
            applicationContext.startService(intent)
        }
    }
}
package com.example.weatherforecast.ui.alert


import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.RadioButton
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.work.*
import com.example.weatherforecast.DataBase.LocalSource
import com.example.weatherforecast.DataBase.RoomState
import com.example.weatherforecast.Model.*
import com.example.weatherforecast.MySharedPreference
import com.example.weatherforecast.MyViewModel
import com.example.weatherforecast.MyViewModelFactory
import com.example.weatherforecast.Network.RemoteSource
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentAlertDialogBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import java.util.Calendar
import java.util.concurrent.TimeUnit


class AlertDialogFragment : AppCompatDialogFragment() {
    private var _binding: FragmentAlertDialogBinding? = null
    private val binding get() = _binding!!


    //    var savedDayFrom = 0
//    var savedMonthFrom = 0
//    var savedYearFrom = 0
//    var savedHourFrom = 0
//    var savedMinuteFrom = 0
//
//    var savedDayTo = 0
//    var savedMonthTo = 0
//    var savedYearTo = 0
//    var savedHourTo = 0
//    var savedMinuteTo = 0
    lateinit var alert: AlertModel


    lateinit var alertDialogViewModel: MyViewModel
    lateinit var alertDialogViewModelFactory: MyViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        alertDialogViewModelFactory = MyViewModelFactory(
            Repository.getInstance(
                RemoteSource.getINSTANCE(),
                LocalSource(requireActivity())
            )
        )
        alertDialogViewModel =
            ViewModelProvider(
                requireActivity(),
                alertDialogViewModelFactory
            ).get(MyViewModel::class.java)
        _binding = FragmentAlertDialogBinding.inflate(inflater, container, false)

        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        settingsManager()
        val pd = ProgressDialog(requireActivity())


//        lifecycleScope.launch {
//            alertDialogViewModel.currentObjHome.collectLatest {
//                when(it){
//                    is RoomState.Loading ->{
//
//                        pd.setMessage("loading")
//                        pd.show()
//                    }
//                    is RoomState.SuccessAlertDialog->{
//                        pd.dismiss()
//                        if (it != null) {
//                            alert.lat = it.data.lat
//                            alert.lon = it.data.lon
//                            binding.alertLocation.text=alert.lat+" "+alert.lon
//                        }
//                    }
//                    else->{
//                        pd.dismiss()
//                        Toast.makeText(requireActivity(),"Can't handle your request", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//        }


        binding.chooseAlertTime.setOnClickListener {

            val cal: Calendar = Calendar.getInstance()

            val hour = cal.get(Calendar.HOUR_OF_DAY)
            val minute = cal.get(Calendar.MINUTE)

            TimePickerDialog(
                requireActivity(),
                TimePickerDialog.OnTimeSetListener { _, hourTo, minuteTo ->
                    val timeTo = TimeUnit.MINUTES.toSeconds(minuteTo.toLong()) +
                            TimeUnit.HOURS.toSeconds(hourTo.toLong()) - (3600L * 2)
                    binding.toTimeD.text = timeToString(timeTo)
                    alert.endTime = timeTo
                },
                hour,
                minute,
                true
            ).show()
            TimePickerDialog(
                requireActivity(),
                TimePickerDialog.OnTimeSetListener { _, hourFrom, minuteFrom ->
                    val timeFrom = TimeUnit.MINUTES.toSeconds(minuteFrom.toLong()) +
                            TimeUnit.HOURS.toSeconds(hourFrom.toLong()) - (3600L * 2)
                    binding.fromTimeDialog.text = timeToString(timeFrom)
                    alert.startTime = timeFrom


                },
                hour,
                minute,
                true
            ).show()
        }
        binding.chooseAlertDate.setOnClickListener {
            val cal: Calendar = Calendar.getInstance()
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val month = cal.get(Calendar.MONTH)
            val year = cal.get(Calendar.YEAR)
            val calTo: Calendar = Calendar.getInstance()
            val calFrom: Calendar = Calendar.getInstance()
            DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { _, yearTo, monthTo, dayTo ->
                    val dateTo = "$dayTo/${monthTo + 1}/$yearTo"
                    binding.toDateDialog.text = dateTo
                    calTo.set(yearTo, monthTo, dayTo)
                    //alert.endDate = dateToLong(dateTo)
                    alert.endDate = calTo.timeInMillis
                },
                year,
                month,
                day
            ).show()

            DatePickerDialog(
                requireActivity(),
                DatePickerDialog.OnDateSetListener { _, yearFrom, monthFrom, dayFrom ->
                    val dateFrom = "$dayFrom/${monthFrom + 1}/$yearFrom"
                    binding.fromDateDialog.text = dateFrom
                    calFrom.set(yearFrom, monthFrom, dayFrom)
                    //alert.startDate = dateToLong(dateFrom)
                    alert.startDate = calFrom.timeInMillis
                },
                year,
                month,
                day
            ).show()


        }
        binding.alertSave.setOnClickListener {
//
            alert.isNotification = MySharedPreference.isNotification()
            alertDialogViewModel.insertAlertInRoom(alert)
            dialog?.dismiss()
        }
        binding.alertCancel.setOnClickListener {
            dialog?.dismiss()
        }

        lifecycleScope.launch {
            alertDialogViewModel.insertAlertObj.collectLatest { id ->
                setPeriodicWorkManager(id)
            }
        }
    }

    private fun setPeriodicWorkManager(id: Long) {
        val data = Data.Builder()
        data.putLong("id",id)

        val constraints=Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .build()
        val periodicWorkRequest=PeriodicWorkRequest.Builder(
            PeriodicWorkManager::class.java,24,TimeUnit.HOURS
        ).setConstraints(constraints)
            .setInputData(data.build())
            .build()

        WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
            "$id",
            ExistingPeriodicWorkPolicy.REPLACE,
            periodicWorkRequest
        )
    }


    private fun setInitialData() {
        val cal: Calendar = Calendar.getInstance()

        val currentHour = TimeUnit.HOURS.toSeconds(cal.get(Calendar.HOUR_OF_DAY).toLong())
        val currentMinute = TimeUnit.MINUTES.toSeconds(cal.get(Calendar.MINUTE).toLong())
        val currentTime = (currentHour + currentMinute).minus(36600L * 2)
        val currentTimeText = timeToString(currentTime + 60)
        val afterOneHour = currentTime.plus(3600L)
        val afterOneHourText = timeToString(afterOneHour)

        val day = cal.get(Calendar.DAY_OF_MONTH)
        val month = cal.get(Calendar.MONTH)
        val year = cal.get(Calendar.YEAR)
        val date = "$day/${month + 1}/$year"
        val dayNow = dateToLong(date)
        val currentDate = dateToString(dayNow)

        alert = AlertModel(currentTime + 60, afterOneHour, dayNow, dayNow, false)

        binding.fromTimeDialog.text = currentTimeText
        binding.fromDateDialog.text = currentDate
        binding.toDateDialog.text = currentDate
        binding.toTimeD.text = afterOneHourText
    }

    override fun onResume() {
        super.onResume()
        // alertDialogViewModel.getHome()
    }


    private fun settingsManager() {
        if (MySharedPreference.isNotification()) {
            binding.radioNotification.isChecked = true
            binding.radioAlarm.isChecked = false

        } else {
            binding.radioNotification.isChecked = false
            binding.radioAlarm.isChecked = true
        }
        binding.radioNotification.setOnCheckedChangeListener { _, Checked ->
            if (Checked) {
                MySharedPreference.setNotification(true)
            }
        }
        binding.radioAlarm.setOnCheckedChangeListener { _, Checked ->
            if (Checked) {
                MySharedPreference.setNotification(false)

                checkPermissionOfOverlay()
            }
        }


    }

    private fun checkPermissionOfOverlay() {
        if (!Settings.canDrawOverlays(requireContext())) {
            val alertDialogBuilder = MaterialAlertDialogBuilder(requireContext())
            alertDialogBuilder.setTitle("Display on top permission")
                .setMessage("let us draw on top")
                .setPositiveButton("Okay") { dialog: DialogInterface, _: Int ->
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + requireContext().applicationContext.packageName)
                    )
                    startActivityForResult(intent, 1)
                    dialog.dismiss()
                }.setNegativeButton("No") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }.show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }
}




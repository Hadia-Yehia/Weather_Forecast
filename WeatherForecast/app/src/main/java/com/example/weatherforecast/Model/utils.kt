package com.example.weatherforecast.Model

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@SuppressLint("SimpleDateFormat")
fun dateToString(dt:Long):String? {
    val cal=Calendar.getInstance()
    cal.timeInMillis=dt
    return SimpleDateFormat("dd/MM/yyyy").format(cal.time)
}
@SuppressLint("SimpleDateFormat")
fun timeToString(dt:Long):String? {
    val timeStamp= Date(TimeUnit.SECONDS.toMillis((dt)))
    return SimpleDateFormat("hh:mm aa").format(timeStamp)
}
fun dateToLong(date:String):Long{

    val simpleDateFormat=SimpleDateFormat("dd/MM/yyyy")
    val timeStamp:Date=simpleDateFormat.parse(date) as Date
    return timeStamp.time
}


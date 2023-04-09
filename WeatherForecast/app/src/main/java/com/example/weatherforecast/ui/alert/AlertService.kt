package com.example.weatherforecast.ui.alert

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.getSystemService
import com.example.weatherforecast.MyActivity
import com.example.weatherforecast.R

class AlertService :Service(){
    private var notificationManager:NotificationManager?=null
    var alertWindowManager:AlertWindowManager?=null


    override fun onBind(p0: Intent?): IBinder? {
        return null
    }
@RequiresApi(Build.VERSION_CODES.M)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)

    //val description=intent?.getStringExtra("description")






    return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        notificationChannel()
        startForeground(1,makeNotification("description"!!))
//        if(Settings.canDrawOverlays(this)){
//            alertWindowManager= AlertWindowManager(this,"description")
//            alertWindowManager!!.intializeWindowManager()}
    }


    private fun makeNotification(description: String): Notification? {
        val intent=Intent(applicationContext,MyActivity::class.java)
        intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        return NotificationCompat.Builder(applicationContext,"2")
            .setSmallIcon(R.drawable.alert)
            .setContentText(description)
            .setContentTitle("Weather Alert")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(description)
            )
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setAutoCancel(true)
            .build()
    }

    private fun notificationChannel() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name :String="WeatherNotification"
            val description="WeatherNotificationDescription"
            val importance=NotificationManager.IMPORTANCE_DEFAULT
            val channel=NotificationChannel("2",name,importance)
            channel.enableVibration(true)
            channel.description=description
            notificationManager=this.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

}

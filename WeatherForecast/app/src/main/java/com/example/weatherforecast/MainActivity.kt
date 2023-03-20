package com.example.weatherforecast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weatherforecast.Network.RemoteSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var remoteSource:RemoteSource= RemoteSource()
        MainScope().launch(Dispatchers.IO){
            remoteSource.getAllWeatherData()
        }

    }
}
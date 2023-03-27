package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.Welcome
import retrofit2.Response

interface RemoteSourceInterface {
    suspend fun getAllWeatherData(lat:String,lon:String):Response<Welcome>
}
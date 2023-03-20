package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.WeatherData
import retrofit2.Response

interface RemoteSourceInterface {
    suspend fun getAllWeatherData():Response<WeatherData>
}
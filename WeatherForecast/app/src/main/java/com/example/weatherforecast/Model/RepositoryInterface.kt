package com.example.weatherforecast.Model

import retrofit2.Response

interface RepositoryInterface {
    suspend fun getAllWeatherData(lat:String,lon:String):Response<Welcome>
}
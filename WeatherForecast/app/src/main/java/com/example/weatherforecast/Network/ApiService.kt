package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("onecall")
    suspend fun getWeatherData(@Query("lat") lat:String,@Query("lon") lon:String,@Query("appid") appid:String="8d77b1fc9f3a0268416450e20e24569d"):Response<Welcome>

}
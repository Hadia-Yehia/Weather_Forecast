package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherData(@Query("lat") lat:String,@Query("lon") lon:String,@Query("appid") appid:String="8d77b1fc9f3a0268416450e20e24569d"):Response<WeatherData>
   /* suspend fun getCoord():Response<Coord>
    @GET("weather")
    suspend fun getWeather():Response<ArrayList<Weather>>
    @GET("main")
    suspend fun getMain():Response<Main>
    @GET("wind")
    suspend fun getWind():Response<Wind>
    @GET("clouds")
    suspend fun getClouds():Response<Clouds>
    @GET("sys")
    suspend fun getSys():Response<Sys>
    @GET("name")
    suspend fun getName():Response<String>*/
}
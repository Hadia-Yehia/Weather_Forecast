package com.example.weatherforecast.Network

import android.util.Log
import com.example.weatherforecast.Model.Welcome
import retrofit2.Response

class RemoteSource:RemoteSourceInterface {
    val api:ApiService by lazy {
        RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    companion object {
        private var instance: RemoteSource? = null
        fun getINSTANCE(): RemoteSource {
            return instance ?: synchronized(this) {
                val temp = RemoteSource()
                instance = temp
                temp
            }
        }
    }
    override suspend fun getAllWeatherData(lat:String,lon:String): Response<Welcome> {
     val response=api.getWeatherData(lat,lon)
        Log.i("TAG", "getAllWeatherData: "+ (response.body()?.current?.weather?.get(0)?.icon
            ?: "no data"))
     return response
    }
}
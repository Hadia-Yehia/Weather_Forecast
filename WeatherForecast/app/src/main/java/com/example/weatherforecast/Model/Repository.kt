package com.example.weatherforecast.Model

import com.example.weatherforecast.DataBase.LocalSourceInterface
import com.example.weatherforecast.Network.RemoteSourceInterface
import retrofit2.Response

class Repository private constructor(var remoteSourceInterface: RemoteSourceInterface):RepositoryInterface {
    //var localSourceInterface: LocalSourceInterface
    companion object{
        private var instance:Repository?=null
        fun getInstance(
            remoteSourceInterface: RemoteSourceInterface,
            //localSourceInterface: LocalSourceInterface
        ):RepositoryInterface{
            return instance?: synchronized(this){
                val temp =Repository(
                    remoteSourceInterface)
                instance=temp
                temp
            }
        }
    }
    override suspend fun getAllWeatherData(): Response<Welcome> {
        return remoteSourceInterface.getAllWeatherData()
    }
}
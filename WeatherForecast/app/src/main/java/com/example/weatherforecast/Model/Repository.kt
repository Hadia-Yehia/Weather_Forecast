package com.example.weatherforecast.Model

import com.example.weatherforecast.DataBase.LocalSourceInterface
import com.example.weatherforecast.Network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

class Repository private constructor(var remoteSourceInterface: RemoteSourceInterface,var localSourceInterface: LocalSourceInterface):RepositoryInterface {
    //var localSourceInterface: LocalSourceInterface
    companion object{
        private var instance:Repository?=null
        fun getInstance(
            remoteSourceInterface: RemoteSourceInterface,
            localSourceInterface: LocalSourceInterface
        ):RepositoryInterface{
            return instance?: synchronized(this){
                val temp =Repository(
                    remoteSourceInterface,localSourceInterface)
                instance=temp
                temp
            }
        }
    }
    override suspend fun getAllWeatherData(lat:String,lon:String): Response<Welcome> {
        return remoteSourceInterface.getAllWeatherData(lat,lon)
    }

    override suspend fun insert(welcome: Welcome) {
        localSourceInterface.insert(welcome)
    }

    override fun getAllStored(): Flow<List<Welcome>> {
        return localSourceInterface.getAllStored()
    }

    override suspend fun delete(welcome: Welcome) {
        localSourceInterface.delete(welcome)
    }
}
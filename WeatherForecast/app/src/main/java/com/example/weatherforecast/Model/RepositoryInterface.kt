package com.example.weatherforecast.Model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {
    fun getAllWeatherData(lat:String,lon:String):Flow<Response<Welcome>>
    suspend fun insert(welcome: Welcome)
    fun getAllStored(): Flow<List<Welcome>>
    suspend fun delete(welcome: Welcome)
    suspend fun insertAlert(alertModel: AlertModel):Long
    fun getAllStoredAlerts(): Flow<List<AlertModel>>
    suspend fun deleteAlert(alertModel: AlertModel)
    fun getAlert(id:Int):AlertModel
    suspend fun deleteHome()
    fun getHome():Flow<HomeModel>
   suspend fun insertHome(homeModel: HomeModel)

}
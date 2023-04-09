package com.example.weatherforecast.DataBase

import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {
    suspend fun insert(welcome: Welcome)
    fun getAllStored(): Flow<List<Welcome>>
    suspend fun delete(welcome: Welcome)

    suspend fun deleteHome()
    fun getHome():Flow<HomeModel>
    suspend fun insertHome(homeModel: HomeModel)
    suspend fun insertAlert(alertModel: AlertModel):Long
    fun getAllStoredAlerts(): Flow<List<AlertModel>>
    suspend fun deleteAlert(alertModel: AlertModel)
    fun getAlert(id:Int):AlertModel


}
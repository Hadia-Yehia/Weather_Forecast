package com.example.weatherforecast.Model

import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface RepositoryInterface {
    suspend fun getAllWeatherData(lat:String,lon:String):Response<Welcome>
    suspend fun insert(welcome: Welcome)
    fun getAllStored(): Flow<List<Welcome>>
    suspend fun delete(welcome: Welcome)

}
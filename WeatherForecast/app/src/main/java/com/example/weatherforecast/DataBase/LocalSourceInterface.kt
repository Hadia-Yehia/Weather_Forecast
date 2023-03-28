package com.example.weatherforecast.DataBase

import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow

interface LocalSourceInterface {
    suspend fun insert(welcome: Welcome)
    fun getAllStored(): Flow<List<Welcome>>
    suspend fun delete(welcome: Welcome)
}
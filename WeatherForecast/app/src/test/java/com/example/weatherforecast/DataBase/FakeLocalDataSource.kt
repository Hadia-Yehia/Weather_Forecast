package com.example.weatherforecast.DataBase

import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeLocalDataSource(var favs: MutableList<Welcome>? = mutableListOf<Welcome>()):LocalSourceInterface {
    override suspend fun insert(welcome: Welcome) {
        favs?.add(welcome)
    }

    override fun getAllStored(): Flow<List<Welcome>> = flow {
        favs?.let { emit(it) }
        }


    override suspend fun delete(welcome: Welcome) {
        favs?.remove(welcome)
    }

    override suspend fun deleteHome() {
        TODO("Not yet implemented")
    }

    override fun getHome(): Flow<HomeModel> {
        TODO("Not yet implemented")
    }

    override suspend fun insertHome(homeModel: HomeModel) {
        TODO("Not yet implemented")
    }

    override suspend fun insertAlert(alertModel: AlertModel): Long {
        TODO("Not yet implemented")
    }

    override fun getAllStoredAlerts(): Flow<List<AlertModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAlert(alertModel: AlertModel) {
        TODO("Not yet implemented")
    }

    override fun getAlert(id: Int): AlertModel {
        TODO("Not yet implemented")
    }
}
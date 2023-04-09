package com.example.weatherforecast.DataBase

import android.content.Context
import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow

class LocalSource(context: Context):LocalSourceInterface {
    private val favDao:FavDao by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getFavDao()
    }
    private val alertDao:AlertDao by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.gerAlertDao()
    }
    private val homeDao:HomeDao by lazy {
        val db:WeatherDataBase=WeatherDataBase.getInstance(context)
        db.getHomeDao()
    }
    override suspend fun insert(welcome: Welcome) {
        favDao.insertFavourite(welcome)
    }

    override fun getAllStored(): Flow<List<Welcome>> {
        return favDao.getAllFavourites()
    }

    override suspend fun delete(welcome: Welcome) {
        favDao.deleteFavourite(welcome)
    }

    override suspend fun deleteHome() {
        homeDao.deleteHome()
    }

    override fun getHome(): Flow<HomeModel> {
        return homeDao.getHome()
    }

    override suspend fun insertHome(homeModel: HomeModel) {
        homeDao.insertHome(homeModel)
    }


    override suspend fun insertAlert(alertModel: AlertModel):Long {
       return alertDao.insertAlert(alertModel)
    }

    override fun getAllStoredAlerts(): Flow<List<AlertModel>> {
       return alertDao.getAllAlerts()
    }

    override suspend fun deleteAlert(alertModel: AlertModel) {
        alertDao.deleteAlert(alertModel.id)
    }

    override fun getAlert(id:Int): AlertModel {
       return alertDao.getAlert(id)
    }

}
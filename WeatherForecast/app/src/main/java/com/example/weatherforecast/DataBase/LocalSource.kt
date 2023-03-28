package com.example.weatherforecast.DataBase

import android.content.Context
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow

class LocalSource(context: Context):LocalSourceInterface {
    private val dao:FavDao by lazy {
        val db:FavouriteDataBase=FavouriteDataBase.getInstance(context)
        db.getFavDao()
    }
    override suspend fun insert(welcome: Welcome) {
        dao.insertFavourite(welcome)
    }

    override fun getAllStored(): Flow<List<Welcome>> {
        return dao.getAllFavourites()
    }

    override suspend fun delete(welcome: Welcome) {
        dao.deleteFavourite(welcome)
    }

}
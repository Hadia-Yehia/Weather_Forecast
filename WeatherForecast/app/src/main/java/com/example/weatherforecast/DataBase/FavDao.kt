package com.example.weatherforecast.DataBase

import androidx.room.*
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDao {
    @Query("SELECT * FROM fav_table")
    fun getAllFavourites(): Flow<List<Welcome>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(welcome: Welcome)

    @Delete
    suspend fun deleteFavourite(welcome: Welcome):Int



}
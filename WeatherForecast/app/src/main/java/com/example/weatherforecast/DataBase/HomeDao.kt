package com.example.weatherforecast.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
    @Query("DELETE FROM home_table")
    suspend fun deleteHome()

    @Query("SELECT * FROM home_table")
    fun getHome(): Flow<HomeModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHome(homeModel: HomeModel)
}
package com.example.weatherforecast.DataBase

import androidx.room.*
import com.example.weatherforecast.Model.Alert
import com.example.weatherforecast.Model.AlertModel
import kotlinx.coroutines.flow.Flow

@Dao
interface AlertDao {
    @Query("SELECT * FROM alert_table")
    fun getAllAlerts(): Flow<List<AlertModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAlert(alertModel: AlertModel):Long

    @Query("DELETE FROM alert_table WHERE id = :id")
    suspend fun deleteAlert(id:Int)

    @Query("SELECT * FROM alert_table WHERE id = :id")
     fun getAlert(id: Int):AlertModel
}
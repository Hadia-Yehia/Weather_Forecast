package com.example.weatherforecast.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.Converters
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Welcome


@Database(entities = arrayOf(Welcome::class,AlertModel::class,HomeModel::class), version = 1)
@TypeConverters(Converters::class)
    abstract class WeatherDataBase: RoomDatabase() {
        abstract fun getFavDao():FavDao
        abstract fun gerAlertDao():AlertDao
        abstract fun getHomeDao():HomeDao
        companion object{
            @Volatile
            private var INSTANCE :WeatherDataBase?=null

            fun getInstance (ctx: Context):WeatherDataBase{
                return INSTANCE?: synchronized(this){
                    val instance= Room.databaseBuilder(
                        ctx.applicationContext,WeatherDataBase::class.java,"weather_database")
                        .build()
                    INSTANCE=instance
                    instance
                }
            }
        }
    }

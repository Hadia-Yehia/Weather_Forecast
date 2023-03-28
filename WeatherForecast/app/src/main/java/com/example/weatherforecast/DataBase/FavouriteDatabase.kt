package com.example.weatherforecast.DataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforecast.Model.Converters
import com.example.weatherforecast.Model.Welcome


@Database(entities = arrayOf(Welcome::class), version = 1)
@TypeConverters(Converters::class)
    abstract class FavouriteDataBase: RoomDatabase() {
        abstract fun getFavDao():FavDao
        companion object{
            @Volatile
            private var INSTANCE :FavouriteDataBase?=null

            fun getInstance (ctx: Context):FavouriteDataBase{
                return INSTANCE?: synchronized(this){
                    val instance= Room.databaseBuilder(
                        ctx.applicationContext,FavouriteDataBase::class.java,"favourite_database")
                        .build()
                    INSTANCE=instance
                    instance
                }
            }
        }
    }

package com.example.weatherforecast.Model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_table")
data class HomeModel(
@Embedded
val welcome: Welcome
)
{
    @PrimaryKey()
    var id:Int=0
}
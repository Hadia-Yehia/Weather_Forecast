package com.example.weatherforecast.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_table")
data class AlertModel(
     var startTime: Long, var endTime:Long, var startDate:Long,
    var endDate:Long, var isNotification:Boolean, ) {
    @PrimaryKey(autoGenerate = true)
    var id:Int=0
    var lat:String ="37.0902"
    var lon:String="-95.7129"
}
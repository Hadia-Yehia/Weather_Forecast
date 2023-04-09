package com.example.weatherforecast.DataBase

import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.Welcome

sealed class RoomState{
    class Success (val data: List<Welcome>): RoomState()
    class SuccessHome(val data:HomeModel?):RoomState()
    class SuccessAlert(val data:List<AlertModel>):RoomState()
    class SuccessAlertDialog(val data:AlertModel):RoomState()
    class Failure(val msg:Throwable): RoomState()
    object Loading: RoomState()
}

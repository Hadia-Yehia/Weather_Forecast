package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.Welcome
import retrofit2.Response

sealed class APIState{
    class Success (val data: Response<Welcome>):APIState()
    class Failure(val msg:Throwable):APIState()
    object Loading:APIState()
}

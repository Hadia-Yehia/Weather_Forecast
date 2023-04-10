package com.example.weatherforecast.Network

import com.example.weatherforecast.Model.Welcome
import retrofit2.Response

class FakeRemoteDataSource(var respose:Welcome):RemoteSourceInterface {
    override suspend fun getAllWeatherData(lat: String, lon: String): Response<Welcome> {
        respose.apply {
            this.lat=lat.toDouble()
            this.lon=lon.toDouble()
        }
        return Response.success(respose)
    }
}


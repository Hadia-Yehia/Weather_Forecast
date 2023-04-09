package com.example.weatherforecast.Model

import com.example.weatherforecast.DataBase.FakeLocalDataSource
import com.example.weatherforecast.MainDispatcherRule
import com.example.weatherforecast.Network.FakeRemoteDataSource
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class RepositoryTest {
    @get:Rule
    val mainDispatcherRule= MainDispatcherRule()
    //repo-> local&remote -> fav lists
    val welcome1= Welcome(
        lat = 64.65, lon = 66.67, timezone = "nominavi", timezoneOffset = 3314, current = Current(
            dt = 5665,
            sunrise = null,
            sunset = null,
            temp = 68.69,
            feelsLike = 70.71,
            pressure = 6448,
            humidity = 3226,
            dew_point = 72.73,
            uvi = 74.75,
            clouds = 3138,
            visibility = 2337,
            wind_speed = 76.77,
            wind_deg = 2134,
            wind_gust = 78.79,
            weather = listOf(),
            pop = null
        ), hourly = listOf(), daily = listOf(), alerts = listOf()
    )
    val welcome2= Welcome(
        lat = 96.97, lon = 98.99, timezone = "decore", timezoneOffset = 1845, current = Current(
            dt = 7293,
            sunrise = null,
            sunset = null,
            temp = 100.101,
            feelsLike = 102.103,
            pressure = 2244,
            humidity = 9768,
            dew_point = 104.105,
            uvi = 106.107,
            clouds = 9163,
            visibility = 5173,
            wind_speed = 108.109,
            wind_deg = 2838,
            wind_gust = 110.111,
            weather = listOf(),
            pop = null
        ), hourly = listOf(), daily = listOf(), alerts = listOf()
    )
    val welcome3:Welcome= Welcome(
        lat = 128.129, lon = 130.131, timezone = "mucius", timezoneOffset = 9376, current = Current(
            dt = 2447,
            sunrise = null,
            sunset = null,
            temp = 132.133,
            feelsLike = 134.135,
            pressure = 1675,
            humidity = 4528,
            dew_point = 136.137,
            uvi = 138.139,
            clouds = 7076,
            visibility = 2448,
            wind_speed = 140.141,
            wind_deg = 9606,
            wind_gust = 142.143,
            weather = listOf(),
            pop = null
        ), hourly = listOf(), daily = listOf(), alerts = listOf()
    )




    val localFavs= listOf(welcome1,welcome2)

    lateinit var fakeRemoteDataSource: FakeRemoteDataSource
    lateinit var fakeLocalDataSource: FakeLocalDataSource

    lateinit var repo:Repository

    @Before
    fun setUp(){
        fakeLocalDataSource= FakeLocalDataSource(localFavs.toMutableList())
        fakeRemoteDataSource=FakeRemoteDataSource(welcome3)

        repo= Repository(
            fakeRemoteDataSource,
            fakeLocalDataSource,
            Dispatchers.Main
        )

    }
    @After
    fun tearDown(){

    }

    @Test
    fun getAllWeatherData() {
    }

    @Test
    fun insert() {
    }

    @Test
    fun getAllStored() {
    }

    @Test
    fun delete() {
    }
}
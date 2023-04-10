package com.example.weatherforecast.Model

import com.example.weatherforecast.DataBase.FakeLocalDataSource
import com.example.weatherforecast.MainDispatcherRule
import com.example.weatherforecast.Network.FakeRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
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

        )

    }
    @Test
    fun getAllWeatherData() =mainDispatcherRule.runBlockingTest{
        // Given
        // When: request weather details from retrofit in repository
        val results = repo.getAllWeatherData(
            lat = "128.129", lon = "130.131"
        ).first()
        results.body().apply {
            this?.lat= "128.129".toDouble()
            this?.lon="130.131".toDouble()
        }

        // Then: response is a same of fake WeatherResponse
        assertThat(results.body(),`is`(welcome3))
    }



    @Test
    fun insert()=mainDispatcherRule.runBlockingTest {

        // Given: item of favorite
        val welcome4:Welcome= Welcome(
            lat = 160.161, lon = 162.163, timezone = "detraxit", timezoneOffset = 6710, current = Current(
                dt = 4388,
                sunrise = null,
                sunset = null,
                temp = 164.165,
                feelsLike = 166.167,
                pressure = 7673,
                humidity = 4323,
                dew_point = 168.169,
                uvi = 170.171,
                clouds = 8842,
                visibility = 3200,
                wind_speed = 172.173,
                wind_deg = 2249,
                wind_gust = 174.175,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )

        // When: insert favorite in room in repository
        repo.insert(welcome4)
        val results=repo.getAllStored().first()

        // Then: size of favorite list will be 5
        assertThat(results.size,`is`(3))
    }

    @Test
    fun getAllStored() =mainDispatcherRule.runBlockingTest{
        // Given
        // When: request all  favorite list in room in repository
        val resutls = repo.getAllStored().first()

        // Then: size of favorite list will be same size 4
        assertThat(resutls.size,`is`(localFavs.size))
        assertThat(resutls.size,`is`(2))
    }

    @Test
    fun delete()=mainDispatcherRule.runBlockingTest {
        val welcome4:Welcome= Welcome(
            lat = 160.161, lon = 162.163, timezone = "detraxit", timezoneOffset = 6710, current = Current(
                dt = 4388,
                sunrise = null,
                sunset = null,
                temp = 164.165,
                feelsLike = 166.167,
                pressure = 7673,
                humidity = 4323,
                dew_point = 168.169,
                uvi = 170.171,
                clouds = 8842,
                visibility = 3200,
                wind_speed = 172.173,
                wind_deg = 2249,
                wind_gust = 174.175,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )

        // When: insert favorite in room in repository
        repo.insert(welcome4)
        val results=repo.getAllStored().first()

        // Then: size of favorite list will be 5
        assertThat(results.size,`is`(3))
        repo.delete(welcome2)
        val result=repo.getAllStored().first()
        assertThat(result.size,`is`(2))


    }
}
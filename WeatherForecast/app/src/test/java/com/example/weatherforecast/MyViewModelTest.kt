package com.example.weatherforecast

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.weatherforecast.DataBase.RoomState
import com.example.weatherforecast.Model.Current
import com.example.weatherforecast.Model.FakeRepository
import com.example.weatherforecast.Model.RepositoryInterface
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.Matchers.`is`
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MyViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
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
    val welcome3: Welcome = Welcome(
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

    lateinit var repo:RepositoryInterface
    lateinit var myViewModel: MyViewModel



    @Before
    fun setUp() {
        repo = FakeRepository(localFavs.toMutableList())
        myViewModel = MyViewModel(repo)
    }



    @Test
    fun getDataFromRoom() =mainDispatcherRule.runBlockingTest{
        myViewModel.getDataFromRoom()
        delay(1000)
        var favs=myViewModel.favObj.first()
            favs=myViewModel.favObj.first()

        assertThat((favs as RoomState.Success).data.size,`is`(2))
    }

    @Test
    fun insertPlaceInRoom()=mainDispatcherRule.runBlockingTest {

        // When insert favorite item in viewModel
       myViewModel.insertPlaceInRoom(welcome3)
        myViewModel.getDataFromRoom()
        delay(1000)
        var favs=myViewModel.favObj.first()
        favs=myViewModel.favObj.first()

        // Then: data of fake favorite list  increase in size than fake data
        assertThat((favs as RoomState.Success).data.size,`is`(3))
    }

    @Test
    fun deletePlaceFromRoom() =mainDispatcherRule.runBlockingTest{
        myViewModel.insertPlaceInRoom(welcome3)
        myViewModel.getDataFromRoom()
        delay(1000)
        var favsBefore=myViewModel.favObj.first()
        favsBefore=myViewModel.favObj.first()

        // Then: data of fake favorite list  increase in size than fake data
        assertThat((favsBefore as RoomState.Success).data.size,`is`(3))
        myViewModel.deletePlaceFromRoom(welcome3)
        myViewModel.getDataFromRoom()
        delay(1000)
        var favsAfter=myViewModel.favObj.first()
        favsAfter=myViewModel.favObj.first()

        // Then: data of fake favorite list  increase in size than fake data
        assertThat((favsAfter as RoomState.Success).data.size,`is`(2))
    }
}
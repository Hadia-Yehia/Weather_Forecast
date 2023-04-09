package com.example.weatherforecast.DataBase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.weatherforecast.Model.Current
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.`is`
import org.hamcrest.collection.IsEmptyCollection
import org.hamcrest.core.IsNull
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class FavDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()


    lateinit var db:WeatherDataBase
    lateinit var dao:FavDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WeatherDataBase::class.java
        ).
        allowMainThreadQueries().build()

        dao = db.getFavDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getAllFavourites()=runBlockingTest {
        // Given
        val welcome1 = Welcome(
            lat = 0.0, lon = 0.0, timezone = "", timezoneOffset = 0, current = Current(
                dt = 0,
                sunrise = null,
                sunset = null,
                temp = 0.0,
                feelsLike = 0.0,
                pressure = 0,
                humidity = 0,
                dew_point = 0.0,
                uvi = 0.0,
                clouds = 0,
                visibility = 0,
                wind_speed = 0.0,
                wind_deg = 0,
                wind_gust = 0.0,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )
        dao.insertFavourite(welcome1)

        val welcome2 = Welcome(
            lat = 1.0, lon = 0.0, timezone = "", timezoneOffset = 0, current = Current(
                dt = 0,
                sunrise = null,
                sunset = null,
                temp = 0.0,
                feelsLike = 0.0,
                pressure = 0,
                humidity = 0,
                dew_point = 0.0,
                uvi = 0.0,
                clouds = 0,
                visibility = 0,
                wind_speed = 0.0,
                wind_deg = 0,
                wind_gust = 0.0,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )
        dao.insertFavourite(welcome2)


        val welcome3 = Welcome(
            lat = 2.0, lon = 0.0, timezone = "", timezoneOffset = 0, current = Current(
                dt = 0,
                sunrise = null,
                sunset = null,
                temp = 0.0,
                feelsLike = 0.0,
                pressure = 0,
                humidity = 0,
                dew_point = 0.0,
                uvi = 0.0,
                clouds = 0,
                visibility = 0,
                wind_speed = 0.0,
                wind_deg = 0,
                wind_gust = 0.0,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )
        dao.insertFavourite(welcome3)

        // When
        val results = dao.getAllFavourites().first()

        // Then
        MatcherAssert.assertThat(results.size, `is`(3))
    }

    @Test
    fun insertFavourite()= runBlockingTest {
        // Given
        val welcome1 = Welcome(
            lat = 0.0, lon = 0.0, timezone = "", timezoneOffset = 0, current = Current(
                dt = 0,
                sunrise = null,
                sunset = null,
                temp = 0.0,
                feelsLike = 0.0,
                pressure = 0,
                humidity = 0,
                dew_point = 0.0,
                uvi = 0.0,
                clouds = 0,
                visibility = 0,
                wind_speed = 0.0,
                wind_deg = 0,
                wind_gust = 0.0,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )
        // When
        dao.insertFavourite(welcome1)

        // Then
        val results = dao.getAllFavourites().first()
        MatcherAssert.assertThat(results[0], IsNull.notNullValue())
    }

    @Test
    fun deleteFavourite() = runBlockingTest{
        // Given
        val welcome1 = Welcome(
            lat = 0.0, lon = 0.0, timezone = "", timezoneOffset = 0, current = Current(
                dt = 0,
                sunrise = null,
                sunset = null,
                temp = 0.0,
                feelsLike = 0.0,
                pressure = 0,
                humidity = 0,
                dew_point = 0.0,
                uvi = 0.0,
                clouds = 0,
                visibility = 0,
                wind_speed = 0.0,
                wind_deg = 0,
                wind_gust = 0.0,
                weather = listOf(),
                pop = null
            ), hourly = listOf(), daily = listOf(), alerts = listOf()
        )
        dao.insertFavourite(welcome1)

        // When
      dao.deleteFavourite(welcome1)
        // Then
        val results = dao.getAllFavourites().first()
        assertThat(results.size,`is`(0))
    }
}
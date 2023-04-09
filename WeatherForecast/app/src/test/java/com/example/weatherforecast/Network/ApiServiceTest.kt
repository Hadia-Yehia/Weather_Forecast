package com.example.weatherforecast.Network

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers.notNullValue
import org.hamcrest.core.Is
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.create

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ApiServiceTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

     var api:ApiService?=null

    @Before
    fun setUp() {
        api=RetrofitHelper.retrofitInstance.create(ApiService::class.java)
    }

    @After
    fun tearDown() {
        api=null
    }

    @Test
    fun getWeatherData() = runBlocking{
        // Given
        val lat = "20"
        val lon = "20"
        val lang="en"
        val unit="metric"

        // When
        val response =  api?.getWeatherData(lat,lon,lang,unit)

        // Then
        assertThat(response?.code() , Is.`is`(200))
        assertThat(response?.body(), notNullValue())

    }
}


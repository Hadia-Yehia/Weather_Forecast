package com.example.weatherforecast.Model

import androidx.room.Entity

// To parse the JSON, install Klaxon and do:
//
//   val welcome = Welcome.fromJson(jsonString)



@Entity(tableName = "fav_table", primaryKeys = ["lat","lon"])
data class Welcome (
    var lat: Double,
    var lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current,
    val hourly: List<Current>,
    val daily: List<Daily>,
    val alerts: List<Alert>?=null,
)

class Alert(var event:String,
            var start: Long,
            var end:Long,
            var lat:Double,
            var lon:Double,
            var description: String)

data class Current (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dew_point: Double,
    val uvi: Double,
    val clouds: Long,
    val visibility: Long,
    val wind_speed: Double,
    val wind_deg: Long,
    val wind_gust: Double,
    val weather: List<Weather>,
    val pop: Double? = null
)

data class Weather (
    val id: Long,
    val main: Main,
    val description: String,
    val icon: String
)

enum class Description(val value: String) {
    BrokenClouds("broken clouds"),
    ClearSky("clear sky"),
    FewClouds("few clouds"),
    LightRain("light rain"),
    OvercastClouds("overcast clouds"),
    ScatteredClouds("scattered clouds");

    companion object {
        public fun fromValue(value: String): Description = when (value) {
            "broken clouds"    -> BrokenClouds
            "clear sky"        -> ClearSky
            "few clouds"       -> FewClouds
            "light rain"       -> LightRain
            "overcast clouds"  -> OvercastClouds
            "scattered clouds" -> ScatteredClouds
            else               -> throw IllegalArgumentException()
        }
    }
}

enum class Main(val value: String) {
    Clear("Clear"),
    Clouds("Clouds"),
    Rain("Rain");

    companion object {
        public fun fromValue(value: String): Main = when (value) {
            "Clear"  -> Clear
            "Clouds" -> Clouds
            "Rain"   -> Rain
            else     -> throw IllegalArgumentException()
        }
    }
}

data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moonPhase: Double,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val windGust: Double,
    val weather: List<Weather>,
    val clouds: Long,
    val pop: Double,
    val uvi: Double,
    val rain: Double? = null
)

data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)




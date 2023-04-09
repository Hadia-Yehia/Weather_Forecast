package com.example.weatherforecast

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

object MySharedPreference {

    private const val NAME = "WeatherSharedPreference"
    private lateinit var sharedPreferences : SharedPreferences
    fun getInstance(activity: Context){
        sharedPreferences = activity.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
    fun isFirstTime():Boolean{
        return sharedPreferences.getBoolean("is First Time",true)
    }
    fun setFirstTime(){
        val editor = sharedPreferences.edit()
        editor.putBoolean("is First Time",false)
        editor.apply()
    }

    fun getWeatherFromMap(): Boolean {
        return sharedPreferences.getBoolean("get weather form map",true)
    }
    fun setWeatherFromMap(boolean: Boolean)  {
        Log.i("TAG", "setWeatherFromMap: $boolean")
        val editor = sharedPreferences.edit()
        editor.putBoolean("get weather form map",boolean)
        editor.apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString("language","en")!!
    }

    fun getUnits(): String {

        return sharedPreferences.getString("unit","default")!!
    }

    fun setLanguage(s: String) {
        val editor = sharedPreferences.edit()
        editor.putString("language",s)
        editor.apply()
    }

    fun setUnit(s: String) {
        val editor = sharedPreferences.edit()
        editor.putString("unit",s)
        editor.apply()
    }

    fun setNotification(s:Boolean){
        val editor= sharedPreferences.edit()
        editor.putBoolean("isNotification",s)
        editor.apply()
    }
    fun isNotification():Boolean{
        return sharedPreferences.getBoolean("isNotification",true)
    }

}
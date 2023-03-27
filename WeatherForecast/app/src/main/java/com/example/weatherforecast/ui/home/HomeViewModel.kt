package com.example.weatherforecast.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.weatherforecast.Model.RepositoryInterface
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(private val repo:RepositoryInterface) : ViewModel() {
    var apiData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()


    fun getDataFromApi(lat:String,lon:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response=repo.getAllWeatherData(lat,lon)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    apiData.value=response.body()
                }
            }
        }
    }
}
class HomeViewModelFactory(private val repo:RepositoryInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            HomeViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}
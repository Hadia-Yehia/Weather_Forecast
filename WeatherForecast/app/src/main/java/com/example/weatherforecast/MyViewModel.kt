package com.example.weatherforecast

import androidx.lifecycle.*
import com.example.weatherforecast.Model.RepositoryInterface
import com.example.weatherforecast.Model.Welcome
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(private val repo:RepositoryInterface) : ViewModel() {
    var apiData: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var apiObj: LiveData<Welcome> = apiData
    var apiDataForFav: MutableLiveData<Welcome> = MutableLiveData<Welcome>()
    var apiObjForFav: LiveData<Welcome> = apiDataForFav
    var favData:MutableLiveData<List<Welcome>> = MutableLiveData()
    var favObj:LiveData<List<Welcome>> =favData
    var current:MutableLiveData<Welcome> = MutableLiveData()
    var currentObj:LiveData<Welcome> =current


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

    fun getDataFromRoom(){
        viewModelScope.launch(Dispatchers.IO){
            repo.getAllStored().collect(){
                favData.postValue(it)
            }
        }
    }
    fun getFromApiForFav(lat:String,lon:String){
        viewModelScope.launch(Dispatchers.IO) {
            val response=repo.getAllWeatherData(lat,lon)
            if(response.isSuccessful){
                withContext(Dispatchers.Main){
                    apiDataForFav.value=response.body()
                }
            }
        }

    }

    fun insertPlaceInRoom(welcome: Welcome){
        viewModelScope.launch  (Dispatchers.IO){
            repo.insert(welcome)
        }
    }
    fun deletePlaceFromRoom(welcome: Welcome){
        viewModelScope.launch(Dispatchers.IO){
            repo.delete(welcome)
            getDataFromRoom()
        }
    }
    fun getFromFav(welcome: Welcome){
        current.postValue(welcome)
    }
}
class MyViewModelFactory(private val repo:RepositoryInterface): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if(modelClass.isAssignableFrom(MyViewModel::class.java)){
            MyViewModel(repo) as T
        }else{
            throw java.lang.IllegalArgumentException("ViewModel Class Not Found")
        }
    }
}

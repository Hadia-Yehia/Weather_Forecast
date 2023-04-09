package com.example.weatherforecast

import android.util.Log
import androidx.lifecycle.*
import com.example.weatherforecast.DataBase.RoomState
import com.example.weatherforecast.Model.AlertModel
import com.example.weatherforecast.Model.HomeModel
import com.example.weatherforecast.Model.RepositoryInterface
import com.example.weatherforecast.Model.Welcome
import com.example.weatherforecast.Network.APIState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel(private val repo:RepositoryInterface) : ViewModel() {
    val apiData: MutableStateFlow<APIState> = MutableStateFlow(APIState.Loading)
    var apiObj: StateFlow<APIState> = apiData

    val apiDataForFav: MutableStateFlow<APIState> = MutableStateFlow(APIState.Loading)
    var apiObjForFav: StateFlow<APIState> = apiDataForFav

    val favData:MutableStateFlow<RoomState> = MutableStateFlow(RoomState.Loading)
    var favObj:StateFlow<RoomState> =favData

    val currentFav:MutableLiveData<Welcome> = MutableLiveData()
    var currentObjFav:LiveData<Welcome> =currentFav

    val alertData:MutableStateFlow<RoomState> = MutableStateFlow(RoomState.Loading)
    var alertObj:StateFlow<RoomState> =alertData

    val currentHome:MutableStateFlow<RoomState> = MutableStateFlow(RoomState.Loading)
    var currentObjHome:StateFlow<RoomState> =currentHome

    val insertAlert:MutableStateFlow<Long> = MutableStateFlow(0)
    var insertAlertObj:StateFlow<Long> =insertAlert



    fun getDataFromApi(lat:String,lon:String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllWeatherData(lat,lon)
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        apiData.value= APIState.Failure(it)
                    }
                    .collect {
                        apiData.value = APIState.Success(it)

                    }
            }
        }
    }


    fun getDataFromRoom(){
        viewModelScope.launch(Dispatchers.IO){
            val result=repo.getAllStored()

            withContext(Dispatchers.Main){
                result.catch {   favData.value= RoomState.Failure(it)}
                    .collect{
                        favData.value = RoomState.Success(it)
                    }
            }
        }
    }
    fun getFromApiForFav(lat:String,lon:String){
        viewModelScope.launch(Dispatchers.IO) {
            val response = repo.getAllWeatherData(lat,lon)
            withContext(Dispatchers.Main) {
                response
                    .catch {
                        apiDataForFav.value= APIState.Failure(it)
                    }
                    .collect {
                        apiDataForFav.value = APIState.Success(it)

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
        currentFav.postValue(welcome)
    }

    fun getAlertsFromRoom(){
        viewModelScope.launch (Dispatchers.IO){
            val result=repo.getAllStoredAlerts()

            withContext(Dispatchers.Main){
                result.catch {   alertData.value= RoomState.Failure(it)}
                    .collect{
                        alertData.value = RoomState.SuccessAlert(it)
                    }
            }

        }

    }
    fun insertAlertInRoom(alertModel: AlertModel){
        viewModelScope.launch  (Dispatchers.IO){
            val id=repo.insertAlert(alertModel)
            insertAlert.value=id
        }
    }

    fun deleteAlertFromRoom(alertModel: AlertModel){
        viewModelScope.launch(Dispatchers.IO){
            repo.deleteAlert(alertModel)
            getAlertsFromRoom()
        }
    }
    fun deleteHome(){
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteHome()
        }
    }
    fun getHome() {
        viewModelScope.launch(Dispatchers.IO){
            val result=repo.getHome()

            withContext(Dispatchers.Main){
                result.catch {   currentHome.value= RoomState.Failure(it)}
                    .collect{
                        Log.i("tag","home collect")
                        currentHome.value = RoomState.SuccessHome(it)
                    }
            }
        }
    }

    fun insertHome(homeModel: HomeModel){
        viewModelScope.launch  (Dispatchers.IO){
            repo.insertHome(homeModel)
        }
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

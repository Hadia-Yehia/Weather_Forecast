package com.example.weatherforecast.Model

import com.example.weatherforecast.DataBase.LocalSourceInterface
import com.example.weatherforecast.Network.RemoteSourceInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

class Repository constructor(
    var remoteSourceInterface: RemoteSourceInterface,
    var localSourceInterface: LocalSourceInterface

) : RepositoryInterface {

    companion object {
        private var instance: Repository? = null
        fun getInstance(
            remoteSourceInterface: RemoteSourceInterface,
            localSourceInterface: LocalSourceInterface
        ): RepositoryInterface {
            return instance ?: synchronized(this) {
                val temp = Repository(
                    remoteSourceInterface, localSourceInterface
                )
                instance = temp
                temp
            }
        }
    }

    override fun getAllWeatherData(lat: String, lon: String): Flow<Response<Welcome>> {
        return flow { emit(remoteSourceInterface.getAllWeatherData(lat, lon)) }
    }

    override suspend fun insert(welcome: Welcome) {
        localSourceInterface.insert(welcome)
    }

    override fun getAllStored(): Flow<List<Welcome>> {
        return localSourceInterface.getAllStored()
    }

    override suspend fun delete(welcome: Welcome) {
        localSourceInterface.delete(welcome)
    }

    override suspend fun insertAlert(alertModel: AlertModel): Long {
        return localSourceInterface.insertAlert(alertModel)
    }

    override fun getAllStoredAlerts(): Flow<List<AlertModel>> {
        return localSourceInterface.getAllStoredAlerts()
    }

    override suspend fun deleteAlert(alertModel: AlertModel) {
        localSourceInterface.deleteAlert(alertModel)
    }

    override fun getAlert(id: Int): AlertModel {
        return localSourceInterface.getAlert(id)
    }

    override suspend fun deleteHome() {
        localSourceInterface.deleteHome()
    }

    override fun getHome(): Flow<HomeModel> {
        return localSourceInterface.getHome()
    }

    override suspend fun insertHome(homeModel: HomeModel) {
        localSourceInterface.insertHome(homeModel)
    }
}
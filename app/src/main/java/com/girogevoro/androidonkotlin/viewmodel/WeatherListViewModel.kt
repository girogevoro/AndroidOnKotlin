package com.girogevoro.androidonkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.girogevoro.androidonkotlin.model.Location
import com.girogevoro.androidonkotlin.model.Repository
import com.girogevoro.androidonkotlin.model.RepositoryLocalImpl
import com.girogevoro.androidonkotlin.model.RepositoryRemoteImpl
import com.girogevoro.androidonkotlin.viewmodel.data.AppState

class WeatherListViewModel : ViewModel() {
    private val TAG = "TAG"
    private lateinit var repository: Repository

    private val liveData: MutableLiveData<AppState> = MutableLiveData<AppState>()

    fun getLiveData(): LiveData<AppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repository = if (isConnection()) {
            RepositoryRemoteImpl()
        } else {
            RepositoryLocalImpl()
        }
    }
    fun getWeatherFromLocalSourceRus() = sentRequest(Location.Russian)

    fun getWeatherFromLocalSourceWorld() = sentRequest(Location.World)

    fun sentRequest(location: Location) {

        liveData.value = AppState.Loading
        if((1..3).random() == 6){
            liveData.postValue(AppState.Error(Throwable("error")))
        }else{
            liveData.postValue(AppState.SuccessMulti(repository.getListWeather(location)))
        }
    }

    private fun isConnection(): Boolean {
        return false
    }



    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "onCleared: ")
    }
}
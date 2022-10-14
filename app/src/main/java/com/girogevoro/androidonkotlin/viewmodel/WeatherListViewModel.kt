package com.girogevoro.androidonkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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

    fun sentRequest() {
        liveData.value = AppState.Loading
        if((1..3).random() == 2){

        }else{
            liveData.postValue(AppState.Success(repository.getWeather(55.755826, 37.617299900000035)))
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
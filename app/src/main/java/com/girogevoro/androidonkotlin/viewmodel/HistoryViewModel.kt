package com.girogevoro.androidonkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.girogevoro.androidonkotlin.App.Companion.getHistoryDao
import com.girogevoro.androidonkotlin.model.repository.LocalRepository
import com.girogevoro.androidonkotlin.model.repository.LocalRepositoryImpl
import com.girogevoro.androidonkotlin.viewmodel.data.AppState

class HistoryViewModel(
    val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepository: LocalRepository = LocalRepositoryImpl(getHistoryDao())
) : ViewModel(){
    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.SuccessMulti(historyRepository.getAllHistory())
    }
}
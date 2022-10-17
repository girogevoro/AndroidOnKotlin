package com.girogevoro.androidonkotlin.viewmodel.data

import com.girogevoro.androidonkotlin.domain.Weather

sealed class AppState {
    data class Success(val weatherData: Weather) : AppState()
    data class SuccessMulti(val weatherList: List<Weather>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
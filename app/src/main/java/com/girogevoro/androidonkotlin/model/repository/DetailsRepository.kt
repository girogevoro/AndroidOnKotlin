package com.girogevoro.androidonkotlin.model.repository

import com.girogevoro.androidonkotlin.model.dto.WeatherDTO

interface DetailsRepository {
    fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: retrofit2.Callback<WeatherDTO>
    )
}

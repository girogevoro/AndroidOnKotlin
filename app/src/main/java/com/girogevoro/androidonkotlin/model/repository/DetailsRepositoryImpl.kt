package com.girogevoro.androidonkotlin.model.repository

import com.girogevoro.androidonkotlin.model.dto.WeatherDTO
import retrofit2.Callback

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource) : DetailsRepository {
    override fun getWeatherDetailsFromServer(
        lat: Double,
        lon: Double,
        callback: Callback<WeatherDTO>
    ) {
        remoteDataSource.getWeatherDetails(lat, lon, callback)
    }

}

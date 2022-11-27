package com.girogevoro.androidonkotlin.model.repository

import com.girogevoro.androidonkotlin.BuildConfig
import com.girogevoro.androidonkotlin.BuildConfig.WEATHER_API_KEY
import com.girogevoro.androidonkotlin.model.dto.WeatherDTO
import com.google.gson.GsonBuilder
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val REQUEST_API_KEY = "X-Yandex-API-Key"
private const val BASE_URL = "https://api.weather.yandex.ru/"

class RemoteDataSource {
    private val weatherApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().setLenient().create()
            )
        )
        .build().create(WeatherAPI::class.java)

    fun getWeatherDetails(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(BuildConfig.WEATHER_API_KEY, lat, lon).enqueue(callback)
    }

}

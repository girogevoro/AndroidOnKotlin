package com.girogevoro.androidonkotlin.model

import com.girogevoro.androidonkotlin.domain.Weather

interface Repository {
    fun getListWeather(): List<Weather>
    fun getWeather(lat: Double, lon: Double): Weather
}

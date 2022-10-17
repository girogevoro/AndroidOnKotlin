package com.girogevoro.androidonkotlin.model

import com.girogevoro.androidonkotlin.domain.Weather

interface Repository {
    fun getListWeather(location: Location): List<Weather>
    fun getWeather(lat: Double, lon: Double): Weather
}

sealed class Location {
    object Russian : Location()
    object World : Location()
}
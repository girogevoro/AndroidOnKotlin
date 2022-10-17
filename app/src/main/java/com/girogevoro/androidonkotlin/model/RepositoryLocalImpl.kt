package com.girogevoro.androidonkotlin.model

import com.girogevoro.androidonkotlin.domain.Weather

class RepositoryLocalImpl : Repository {
    override fun getListWeather(): List<Weather> {
        return listOf(Weather())
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }

}

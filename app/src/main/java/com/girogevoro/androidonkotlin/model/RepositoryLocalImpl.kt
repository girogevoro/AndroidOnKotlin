package com.girogevoro.androidonkotlin.model

import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.domain.getRussianCities
import com.girogevoro.androidonkotlin.domain.getWorldCities

class RepositoryLocalImpl : Repository {
    override fun getListWeather(location: Location): List<Weather> {
        return when (location) {
            Location.Russian -> getRussianCities()
            Location.World -> getWorldCities()
        }
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }

}

package com.girogevoro.androidonkotlin.utils

import com.girogevoro.androidonkotlin.domain.City
import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.domain.getDefaultCity
import com.girogevoro.androidonkotlin.model.dto.FactDTO
import com.girogevoro.androidonkotlin.model.dto.WeatherDTO
import com.girogevoro.androidonkotlin.model.room.HistoryEntity

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact: FactDTO = weatherDTO.fact
    return listOf(Weather(getDefaultCity(), fact.temp, fact.feels_like, fact.condition))
}

fun convertWeatherToEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0, weather.city.name, weather.temperature, weather.condition)
}

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(City(it.city, 0.0, 0.0), it.temperature, 0, it.condition)
    }
}
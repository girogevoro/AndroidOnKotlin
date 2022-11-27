package com.girogevoro.androidonkotlin.utils

import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.domain.getDefaultCity
import com.girogevoro.androidonkotlin.model.dto.WeatherDTO

fun convertDtoToModel(weatherDTO: WeatherDTO): List<Weather> {
    val fact = weatherDTO.fact
    return listOf(Weather(getDefaultCity(), fact.temp, fact.feels_like))
}
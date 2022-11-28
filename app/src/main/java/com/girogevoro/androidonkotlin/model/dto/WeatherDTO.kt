package com.girogevoro.androidonkotlin.model.dto

data class WeatherDTO(
    val fact: FactDTO,
    val forecast: Forecast,
    val info: Info,
    val now: Int,
    val now_dt: String
)
package com.girogevoro.androidonkotlin.model.dto

data class Fact(
    val condition: String,
    val daytime: String,
    val feels_like: Int,
    val humidity: Double,
    val icon: String,
    val obs_time: Double,
    val polar: Boolean,
    val pressure_mm: Double,
    val pressure_pa: Double,
    val season: String,
    val temp: Int,
    val wind_dir: String,
    val wind_gust: Double,
    val wind_speed: Double
)
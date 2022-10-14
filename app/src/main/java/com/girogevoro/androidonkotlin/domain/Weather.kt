package com.girogevoro.androidonkotlin.domain

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 20,
    val feelsLike: Int = 30
)

data class City(
    val name: String,
    val lat: Double,
    val lon: Double
)

fun getDefaultCity(): City {
    return City("Москва", 55.755826, 37.617299900000035)
}

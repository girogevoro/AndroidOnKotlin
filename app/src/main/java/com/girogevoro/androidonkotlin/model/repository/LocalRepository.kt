package com.girogevoro.androidonkotlin.model.repository

import com.girogevoro.androidonkotlin.domain.Weather

interface LocalRepository {
    fun getAllHistory() : List<Weather>
    fun saveEntity(weather: Weather)
}
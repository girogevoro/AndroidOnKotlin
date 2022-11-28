package com.girogevoro.androidonkotlin.model.repository

import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.model.room.HistoryDao
import com.girogevoro.androidonkotlin.utils.convertHistoryEntityToWeather
import com.girogevoro.androidonkotlin.utils.convertWeatherToEntity

class LocalRepositoryImpl (private val localDataSource : HistoryDao) : LocalRepository{
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }
}
package com.girogevoro.androidonkotlin.view

import com.girogevoro.androidonkotlin.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}

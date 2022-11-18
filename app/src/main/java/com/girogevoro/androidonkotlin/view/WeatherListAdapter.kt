package com.girogevoro.androidonkotlin.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.girogevoro.androidonkotlin.databinding.FragmentWeatherListRecyclerItemBinding
import com.girogevoro.androidonkotlin.domain.Weather
import com.google.gson.annotations.Until

class WeatherListAdapter(private val dataList: List<Weather>, private val pressItem: (weather:Weather)->Any) :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding =
            FragmentWeatherListRecyclerItemBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WeatherViewHolder(private val binding: FragmentWeatherListRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weather: Weather) {
            with(binding) {
                cityName.text = weather.city.name
                root.setOnClickListener {
                    pressItem(weather)
                }
            }
        }
    }

}
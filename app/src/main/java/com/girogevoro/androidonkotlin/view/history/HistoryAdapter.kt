package com.girogevoro.androidonkotlin.view.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.girogevoro.androidonkotlin.databinding.FragmentHistoryRecyclerItemBinding

import com.girogevoro.androidonkotlin.domain.Weather

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private var data: List<Weather> = arrayListOf()

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            FragmentHistoryRecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

    inner class HistoryViewHolder(private val binding: FragmentHistoryRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Weather) {
            binding.apply {
                cityName.text = data.city.name
                weatherCondition.text = data.condition
                weatherTemperature.text = data.temperature.toString()
            }
        }

    }
}
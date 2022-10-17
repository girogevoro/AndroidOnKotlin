package com.girogevoro.androidonkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.girogevoro.androidonkotlin.databinding.FragmentWeatherListBinding
import com.girogevoro.androidonkotlin.viewmodel.WeatherListViewModel
import com.girogevoro.androidonkotlin.viewmodel.data.AppState

class WeatherListFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    private lateinit var viewModel: WeatherListViewModel
    private lateinit var binding:FragmentWeatherListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<AppState> {
            override fun onChanged(appState: AppState) {
                renderData(appState)
            }
        })
        viewModel.sentRequest()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {}
            AppState.Loading -> {}
            is AppState.Success -> {
                val result = appState.weatherData
                binding.cityName.text = result.temperature.toString()
                binding.temperatureValue.text = result.temperature.toString()
                binding.feelsLikeValue.text = result.feelsLike.toString()
                binding.cityCoordinates.text = "${result.city.lat}/${result.city.lon}"
            }
        }


    }

}
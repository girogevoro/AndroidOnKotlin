package com.girogevoro.androidonkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.girogevoro.androidonkotlin.databinding.FragmentDetailsBinding
import com.girogevoro.androidonkotlin.domain.Weather

class DetailsFragment:Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA)
        if(weather != null){
            renderData(weather)
        }
    }

    private fun renderData(weather: Weather) {
        binding.cityName.text = weather.city.name
        binding.temperatureValue.text = weather.temperature.toString()
        binding.feelsLikeValue.text = weather.feelsLike.toString()
        binding.cityCoordinates.text = "${weather.city.lat}/${weather.city.lon}"
    }

    companion object {
        const val BUNDLE_WEATHER_EXTRA = "weather"
        fun newInstance(weather: Weather):DetailsFragment {
            val bundle = Bundle()
            bundle.putParcelable(BUNDLE_WEATHER_EXTRA,weather)
            val fr = DetailsFragment()
            fr.arguments = bundle
            return fr
        }
    }
}
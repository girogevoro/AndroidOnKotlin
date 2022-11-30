package com.girogevoro.androidonkotlin.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.girogevoro.androidonkotlin.R
import com.girogevoro.androidonkotlin.databinding.FragmentDetailsBinding
import com.girogevoro.androidonkotlin.domain.*
import com.girogevoro.androidonkotlin.model.dto.WeatherDTO
import com.girogevoro.androidonkotlin.utils.hide
import com.girogevoro.androidonkotlin.utils.show
import com.girogevoro.androidonkotlin.utils.showSnackBar
import com.girogevoro.androidonkotlin.viewmodel.DetailsViewModel
import com.girogevoro.androidonkotlin.viewmodel.data.AppState

const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"
const val DETAILS_TEMP_EXTRA = "TEMPERATURE"
const val DETAILS_FEELS_LIKE_EXTRA = "FEELS LIKE"
const val DETAILS_CONDITION_EXTRA = "CONDITION"
private const val TEMP_INVALID = -100
private const val FEELS_LIKE_INVALID = -100
private const val PROCESS_ERROR = "Обработка ошибки"


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    private lateinit var city: City

    private val viewModel: DetailsViewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
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

        val weather = arguments?.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA) ?: Weather()
        city = weather.city

        viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherFromRemoteSource(city.lat, city.lon)
    }


    private fun renderData(appState: AppState) {
        binding.mainView.show()
        binding.loadingLayout.hide()

        when (appState) {
            is AppState.SuccessMulti -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                setWeather(appState.weatherList[0])
            }
            is AppState.Success -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                setWeather(appState.weatherData)
            }
            is AppState.Loading -> {
                binding.mainView.hide()
                binding.loadingLayout.show()
            }
            is AppState.Error -> {
                binding.mainView.show()
                binding.loadingLayout.hide()
                binding.mainView.showSnackBar(
                    getString(R.string.error),
                    getString(R.string.reload)
                ) {
                    viewModel.getWeatherFromRemoteSource(city.lat, city.lon)
                }
            }
        }

    }

    private fun setWeather(weather: Weather) {
        with(binding) {
            cityName.text = city.name
            "${city.lat}/${city.lon}".also { cityCoordinates.text = it }
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()

            Glide.with(requireActivity())
                .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
                .into(headerIcon)
            saveCity(city, weather)
        }
    }

    private fun saveCity(city: City, weather: Weather){
        viewModel.saveCityToDb(Weather(city, weather.temperature, weather.feelsLike, weather.condition))
    }


    companion object {
        const val BUNDLE_WEATHER_EXTRA = "weather"
        fun newInstance(weather: Weather): DetailsFragment {
            val bundle = Bundle().apply {
                putParcelable(BUNDLE_WEATHER_EXTRA, weather)
            }

            return DetailsFragment().apply { arguments = bundle }
        }
    }


}
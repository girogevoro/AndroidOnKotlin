package com.girogevoro.androidonkotlin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.girogevoro.androidonkotlin.BuildConfig
import com.girogevoro.androidonkotlin.databinding.FragmentDetailsBinding
import com.girogevoro.androidonkotlin.domain.Weather
import com.girogevoro.androidonkotlin.model.dto.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Collectors

class DetailsFragment : Fragment() {
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

        weather?.run {
            requestWeather(city.lat, city.lat){
                temperature = it.fact.temp
                feelsLike = it.fact.feels_like
                startRender(this)
            }
        }

    }

    private fun startRender(weather: Weather?) {
        weather?.run {
            requireActivity().runOnUiThread {
                renderData(this)
            }
        }
    }

    private fun renderData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            "${weather.city.lat}/${weather.city.lon}".also { cityCoordinates.text = it }
        }

    }


    //FIXME out from fragment to repository
    fun requestWeather(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {
        val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        var myConnection: HttpURLConnection? = null

        myConnection = uri.openConnection() as HttpURLConnection
        myConnection.readTimeout = 5000
        myConnection.addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
        Thread {
            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
            val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
            block(weatherDTO)
        }.start()
    }

    fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
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
package com.girogevoro.androidonkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.girogevoro.androidonkotlin.model.repository.DetailsRepository
import com.girogevoro.androidonkotlin.model.repository.DetailsRepositoryImpl
import com.girogevoro.androidonkotlin.model.repository.RemoteDataSource
import com.girogevoro.androidonkotlin.model.dto.WeatherDTO
import com.girogevoro.androidonkotlin.utils.convertDtoToModel
import com.girogevoro.androidonkotlin.viewmodel.data.AppState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

private const val SERVER_ERROR = "Ошибка сервера"
private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class DetailsViewModel(
    private val detailsLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
) : ViewModel() {
    fun getLiveData() = detailsLiveData

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(lat, lon, callBack)
    }

    private val callBack = object : Callback<WeatherDTO> {

        @Throws(IOException::class)
        override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    AppState.Error(Throwable(SERVER_ERROR))
                }
            )
        }

        override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
            detailsLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }

        private fun checkResponse(weatherDTO: WeatherDTO): AppState {
            val fact = weatherDTO.fact
            return if (fact == null || fact.temp == null || fact.feels_like == null || fact.condition.isNullOrEmpty()) {
                AppState.Error(Throwable(CORRUPTED_DATA))
            } else {
                AppState.SuccessMulti(convertDtoToModel(weatherDTO))
            }
        }
    }
}
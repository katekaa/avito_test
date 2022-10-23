package com.example.weather.viewmodel

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weather.model.SingleForecast
import com.example.weather.model.network.Api
import com.example.weather.view.MainActivity.Key.apiKey
import kotlinx.coroutines.launch

enum class ApiStatus(val loader: Int, val layout: Int) {
    LOADING(View.VISIBLE, View.GONE),
    ERROR(View.GONE, View.VISIBLE),
    DONE(View.GONE, View.VISIBLE)
}

@RequiresApi(Build.VERSION_CODES.O)
class ForecastViewModel : ViewModel() {

    private val _forecast = MutableLiveData<SingleForecast>()
    val forecast: LiveData<SingleForecast> = _forecast
    private val _city = MutableLiveData<String>()
    val city: LiveData<String> = _city
    private val _weekForecast = MutableLiveData<List<SingleForecast>>()
    val weekForecast: LiveData<List<SingleForecast>> = _weekForecast

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus> = _status
    private val _weekStatus = MutableLiveData<ApiStatus>()
    val weekStatus: LiveData<ApiStatus> = _weekStatus

    init {
        _forecast.value = SingleForecast("", "", "", "", "", "", "", "", "", false)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecast(lat: Double, long: Double) {
        viewModelScope.launch {
            _status.value = ApiStatus.LOADING
            try {
                _forecast.value = Api.retrofitService.getCurrentForecast(
                    lat,
                    long,
                    apiKey
                ).toForecast()
                setCity(lat, long)
                _status.value = ApiStatus.DONE

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getForecastByCity(city: String) {
        _status.value = ApiStatus.LOADING
        viewModelScope.launch {
            try {
                val result = Api.retrofitService.getForecastByCity(city, apiKey)
                _forecast.value = result.toForecast()
                _city.value = city
                _status.value = ApiStatus.DONE

            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekForecast(lat: Double, long: Double) {
        viewModelScope.launch {
            _weekStatus.value = ApiStatus.LOADING
            try {
                val result = Api.retrofitService.getWeekForecast(
                    lat,
                    long,
                    apiKey
                ).list.map { it.toForecast() }
                _weekForecast.value = mapWeekForecast(result)
                _weekStatus.value = ApiStatus.DONE

            } catch (e: Exception) {
                _weekForecast.value = listOf()
                _weekStatus.value = ApiStatus.ERROR
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeekForecastByCity(city: String) {
        viewModelScope.launch {
            _weekStatus.value = ApiStatus.LOADING
            try {
                val result = Api.retrofitService.getWeekForecastByCity(
                    city,
                    apiKey
                ).list.map { it.toForecast() }
                _weekForecast.value = mapWeekForecast(result)
                _weekStatus.value = ApiStatus.DONE

            } catch (e: Exception) {
                _weekStatus.value = ApiStatus.ERROR
            }
        }
    }

    fun setCity(lat: Double, long: Double) {
        viewModelScope.launch {
            try {
                _city.value =
                    Api.retrofitService.getCity(lat, long, apiKey).map { it.local_names.ru }[0]

            } catch (e: Exception) {
                _city.value = ""
            }
        }
    }

    private fun mapWeekForecast(list: List<SingleForecast>): List<SingleForecast> {
        var currDate = ""
        for (i in list) {
            if (currDate != i.date) {
                currDate = i.date
                i.flag = true
            }
        }
        return list
    }
}
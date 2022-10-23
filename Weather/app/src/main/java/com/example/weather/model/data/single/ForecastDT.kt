package com.example.weather.model.data.single

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.model.SingleForecast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


data class ForecastDT(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val coord: Pair<Double, Double>,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val rain: Rain,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
{
    @RequiresApi(Build.VERSION_CODES.O)
    fun toForecast(): SingleForecast {
        val myLocale = Locale("ru","RU")
        return SingleForecast(
            temperature = main.temp.roundToInt().toString(),
            minTemperature = main.temp_min.roundToInt().toString(),
            maxTemperature = main.temp_max.roundToInt().toString(),
            feelsLike = main.feels_like.roundToInt().toString(),
            description = weather[0].description,
            date = SimpleDateFormat("d MMMM, EEEE", myLocale).format(dt.toLong() * 1000),
            updated = SimpleDateFormat("HH:mm", myLocale).format(dt.toLong() * 1000),
            wind = wind.speed.toString(),
            icon = "i"+weather[0].icon,
            flag = true
        )
    }
}
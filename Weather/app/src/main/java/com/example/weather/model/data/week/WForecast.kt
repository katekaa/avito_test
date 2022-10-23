package com.example.weather.model.data.week

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.model.SingleForecast
import com.example.weather.model.data.single.Clouds
import com.example.weather.model.data.single.Weather
import com.example.weather.model.data.single.Wind
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

data class WForecast (
    val clouds: Clouds,
    val dt: Int,
    val dt_txt: String,
    val main: Main,
    val pop: Double,
    val rain: Rain,
    val sys: Sys,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun toForecast(): SingleForecast {
        val myLocale = Locale("ru","RU")
        return SingleForecast(
            temperature = main.temp.roundToInt().toString(),
            minTemperature = main.temp_min.roundToInt().toString(),
            maxTemperature = main.temp_max.roundToInt().toString(),
            feelsLike = main.feels_like.roundToInt().toString(),
            description = State.valueOf(weather[0].main).russian,
            date = SimpleDateFormat("d MMMM, EEEE", myLocale).format(dt.toLong() * 1000),
            updated = SimpleDateFormat("HH:mm", myLocale).format(dt.toLong() * 1000),
            wind = wind.speed.toString(),
            icon = "i"+weather[0].icon,
            flag = false
        )
    }

    enum class State(val russian: String) {
        Rain("Дождь"),
        Thunderstorm("Гроза"),
        Drizzle("Морось"),
        Snow("Снег"),
        Mist("Мгла"),
        Smoke("Дым"),
        Haze("Дымка"),
        Dust("Пыль"),
        Fog("Туман"),
        Sand("Песок"),
        Ash("Пепел"),
        Squall("Шквал"),
        Tornado("Торнадо"),
        Clear("Ясно"),
        Clouds("Облачно")

    }
}
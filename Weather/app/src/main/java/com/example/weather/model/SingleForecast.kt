package com.example.weather.model

data class SingleForecast(
    val temperature: String,
    val minTemperature: String,
    val maxTemperature: String,
    val feelsLike: String,
    val description: String,
    val date: String,
    val updated: String,
    val wind: String,
    val icon: String?,
    var flag: Boolean
)
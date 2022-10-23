package com.example.weather.model.data.week

data class WeekForecastDT(
    val city: City,
    val cnt: Int,
    val cod: String,
    val list: List<WForecast>,
    val message: Int
)
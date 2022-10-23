package com.example.weather.model.data.week

data class City(
    val coord: Pair<Double, Double>,
    val country: String,
    val id: Int,
    val name: String,
    val population: Int,
    val sunrise: Int,
    val sunset: Int,
    val timezone: Int
)
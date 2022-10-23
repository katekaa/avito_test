package com.example.weather.model.data.city

class CityDT : ArrayList<CityDTItem>()

data class CityDTItem(
    val country: String,
    val lat: Double,
    val local_names: LocalNames,
    val lon: Double,
    val name: String
)
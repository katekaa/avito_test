package com.example.weather.model.network

import com.example.weather.model.data.city.CityDT
import com.example.weather.model.data.single.ForecastDT
import com.example.weather.model.data.week.WeekForecastDT
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = "https://api.openweathermap.org/"

private val logging = run {
    val httpLoggingInterceptor = HttpLoggingInterceptor()
    httpLoggingInterceptor.apply {
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
    }
}
val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(logging).build()

private val retrofit = Retrofit.Builder()
    .client(client)
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL).build()

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getCurrentForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): ForecastDT

    @GET("data/2.5/weather")
    suspend fun getForecastByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): ForecastDT

    @GET("data/2.5/forecast")
    suspend fun getWeekForecast(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String,
        @Query("cnt") cnt: Int = 35,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): WeekForecastDT

    @GET("data/2.5/forecast")
    suspend fun getWeekForecastByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("cnt") cnt: Int = 35,
        @Query("units") units: String = "metric",
        @Query("lang") lang: String = "ru",
    ): WeekForecastDT

    @GET("geo/1.0/reverse")
    suspend fun getCity(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("appid") apiKey: String
    ): CityDT
}

object Api {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

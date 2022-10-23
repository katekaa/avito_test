package com.example.weather.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.weather.BuildConfig
import com.example.weather.R

class MainActivity : AppCompatActivity() {

    companion object Key {
        const val apiKey = "fb47d6fd6b4e84ba1f270df7581e72a0"
        const val defaultLat = 55.751244
        const val defaultLon = 37.618423
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
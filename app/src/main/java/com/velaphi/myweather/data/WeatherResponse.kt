package com.velaphi.myweather.data

data class WeatherResponse(
    val base: String,
    val main: Main,
    val weather: List<Weather>
)
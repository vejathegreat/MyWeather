package com.velaphi.myweather.data

data class DayWeather (
    val dt: Int,
    val main: Main,
    val weather: List<Weather>
)
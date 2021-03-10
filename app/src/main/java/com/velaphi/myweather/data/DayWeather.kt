package com.velaphi.myweather.data

data class DayWeather (
    val dt: Long,
    val main: Main,
    val weather: List<Weather>
)
package com.velaphi.myweather.api

import com.velaphi.myweather.data.FiveDayForecastResponse
import com.velaphi.myweather.data.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {
    companion object{
        const val LONGITUDE = "lon"
        const val LATITUDE = "lat"
        const val UNITS = "units"
    }

    @GET("weather?")
    fun getWeather(@Query(LATITUDE) latitude: String, @Query(LONGITUDE) longitude: String, @Query(
        UNITS) units: String): Single<WeatherResponse>

    @GET("forecast?")
    fun getForecast(@Query(LATITUDE) latitude: String, @Query(LONGITUDE) longitude: String, @Query(
        UNITS) units: String): Single<FiveDayForecastResponse>
}
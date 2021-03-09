package com.velaphi.myweather.api

import com.velaphi.myweather.data.ForecastResponse
import com.velaphi.myweather.data.WeatherResponse
import io.reactivex.Single
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIClient {

    companion object{
        const val  BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(WeatherAPI::class.java)

    fun getCurrentWeather(
        latitude: String,
        longitude: String,
        units: String
    ): Single<WeatherResponse> {
        return api.getWeatherByGPS(latitude, longitude, units)
    }

    fun getFiveDayForecast(
        latitude: String,
        longitude: String,
        units: String
    ): Single<ForecastResponse> {
        return api.getForecastByGPS(latitude, longitude, units)
    }

    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(RequestInterceptor())
        return client.build()
    }

}
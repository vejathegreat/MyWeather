package com.velaphi.myweather.api

import com.velaphi.myweather.data.CombineResponse
import com.velaphi.myweather.data.FiveDayForecastResponse
import com.velaphi.myweather.data.WeatherResponse
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class WeatherAPIClient {

    companion object {
        const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    }

    private val api = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient())
        .build()
        .create(WeatherAPI::class.java)

    private fun getCurrentWeather(
        latitude: String,
        longitude: String,
        units: String
    ): Single<WeatherResponse> {
        return api.getWeather(latitude, longitude, units)
    }

    private fun getFiveDayForecast(
        latitude: String,
        longitude: String,
        units: String
    ): Single<FiveDayForecastResponse> {
        return api.getForecast(latitude, longitude, units)
    }

    fun getCombinedResponse(
        latitude: String,
        longitude: String,
        units: String
    ): Single<CombineResponse> {
        val currentWeatherResponse = getCurrentWeather(latitude, longitude, units)
        val forecastResponse = getFiveDayForecast(latitude, longitude, units)

        return Single.zip(
            currentWeatherResponse,
            forecastResponse,
            BiFunction<WeatherResponse, FiveDayForecastResponse, CombineResponse> { currentWeather: WeatherResponse, fiveDayForecast: FiveDayForecastResponse ->
                CombineResponse(currentWeather, fiveDayForecast)
            })
    }


    private fun getOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.addInterceptor(RequestInterceptor())
        return client.build()
    }

}
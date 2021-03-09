package com.velaphi.myweather.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.velaphi.myweather.api.WeatherAPIClient
import com.velaphi.myweather.data.CombineResponse
import com.velaphi.myweather.data.FiveDayForecastResponse
import com.velaphi.myweather.data.WeatherResponse
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers


class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val apiClient = WeatherAPIClient()
    private val disposable = CompositeDisposable()

    val combineResponseData = MutableLiveData<CombineResponse>()
    val loadingIndicator = MutableLiveData<Boolean>()

    fun getForecastData(latitude: String, longitude: String, units: String) {
        loadingIndicator.value = true
        disposable.add(apiClient.getCombinedResponse(latitude, longitude, units)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<CombineResponse>() {
                    override fun onSuccess(combinedResponse: CombineResponse) {
                        loadingIndicator.value = false
                        combineResponseData.value = combinedResponse
                    }

                    override fun onError(e: Throwable) {
                        Log.i("Error : ", e.message + " " + e.printStackTrace())
                    }

                }))
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}
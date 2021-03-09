package com.velaphi.myweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.velaphi.myweather.R
import com.velaphi.myweather.data.CombineResponse
import com.velaphi.myweather.utils.Constant.METRIC
import com.velaphi.myweather.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        viewModel.getForecastData("-26.107567","28.056702",METRIC)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.combineResponseData.observe(this, Observer {
            process(it)
        })
    }

    private fun process(it: CombineResponse?) {
        it?.weatherResponse?.base?.let { it1 -> Log.i("XXX", it1) }
    }
}
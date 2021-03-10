package com.velaphi.myweather.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.velaphi.myweather.R
import com.velaphi.myweather.data.CombineResponse
import com.velaphi.myweather.data.WeatherResponse
import com.velaphi.myweather.databinding.ActivityMainBinding
import com.velaphi.myweather.utils.Constant
import com.velaphi.myweather.viewModel.WeatherViewModel
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel
    private lateinit var dataBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        val intent = intent
        val latitude = intent.getStringExtra(EXTRA_LATITUDE)
        val longitude = intent.getStringExtra(EXTRA_LONGITUDE)
        viewModel.getForecastData(latitude.toString(), longitude.toString(), Constant.METRIC)

        setupObservers()
    }

    private fun setupObservers() {
        viewModel.combineResponseData.observe(this, Observer {
            processResponse(it)
        })
    }

    private fun processResponse(combineResponse: CombineResponse?) {
        displayCurrentWeather(combineResponse?.weatherResponse)
    }

    private fun displayCurrentWeather(weatherResponse: WeatherResponse?) {
        dataBinding.textViewDescription.text = weatherResponse?.weather?.get(0)?.main
        dataBinding.textViewCurrentWeather.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp?.roundToInt())
        dataBinding.textViewMinTemperature.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp_min?.roundToInt())
        dataBinding.textViewCurrentTemperature.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp?.roundToInt())
        dataBinding.textViewMaxTemperature.text =
            this.getString(R.string.degree, weatherResponse?.main?.temp_max?.roundToInt())

    }

    companion object {
        private const val EXTRA_LATITUDE = "EXTRA_LATITUDE"
        private const val EXTRA_LONGITUDE = "EXTRA_LONGITUDE"
        fun getStartIntent(context: Context, latitude: String?, longitude: String?): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.putExtra(EXTRA_LATITUDE, latitude)
            intent.putExtra(EXTRA_LONGITUDE, longitude)
            return intent
        }
    }
}
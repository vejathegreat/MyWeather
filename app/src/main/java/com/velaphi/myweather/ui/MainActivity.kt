package com.velaphi.myweather.ui

import android.content.Context
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.velaphi.myweather.R
import com.velaphi.myweather.data.CombineResponse
import com.velaphi.myweather.data.FiveDayForecastResponse
import com.velaphi.myweather.data.WeatherResponse
import com.velaphi.myweather.databinding.ActivityMainBinding
import com.velaphi.myweather.utils.Constant
import com.velaphi.myweather.utils.changeLayoutBackground
import com.velaphi.myweather.utils.transformList
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
        viewModel.loadingIndicator.observe(this, Observer {
            if (it) {
                dataBinding.progressBar.visibility = View.GONE
            } else {
                dataBinding.progressBar.visibility = View.VISIBLE
            }
        })
        viewModel.combineResponseData.observe(this, Observer {
            dataBinding.mainContainer.visibility = View.VISIBLE
            processResponse(it)
        })
    }

    private fun processResponse(combineResponse: CombineResponse?) {
        displayCurrentWeather(combineResponse?.weatherResponse)
        displayForecast(combineResponse?.fiveDayForecastResponse)
    }

    private fun displayForecast(fiveDayForecastResponse: FiveDayForecastResponse?) {
        val filteredList = fiveDayForecastResponse?.list?.let { transformList(it) }
        val forecastRecyclerView = findViewById<RecyclerView>(R.id.recyclerView_forecast)

        var forecastAdapter = ForecastAdapter(filteredList!!)
        forecastRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = forecastAdapter
        }
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
        val linearLayout = findViewById<LinearLayout>(R.id.main_container)
        weatherResponse?.weather?.get(0)?.icon?.let {
            changeLayoutBackground(
                this,
                it, linearLayout
            )
        }
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
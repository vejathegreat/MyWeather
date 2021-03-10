package com.velaphi.myweather.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.velaphi.myweather.R
import com.velaphi.myweather.data.CombineResponse
import com.velaphi.myweather.viewModel.WeatherViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: WeatherViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(WeatherViewModel::class.java)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.combineResponseData.observe(this, Observer {
            process(it)
        })
    }

    private fun process(combineResponse: CombineResponse?) {
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
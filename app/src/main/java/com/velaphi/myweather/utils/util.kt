package com.velaphi.myweather.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.velaphi.myweather.R
import java.util.*

fun isConnected(context: Context?): Boolean {
    val cm = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun getDayNumberOld(datetime: Long): String {
    val calender = Calendar.getInstance()
    calender.time = Date(datetime * 1000L)
    val dayOfWeek = calender[Calendar.DAY_OF_WEEK]
    return (getDayOfWeek(dayOfWeek))
}

private fun getDayOfWeek(dayOfWeek: Int): String {
    var day = ""
    when (dayOfWeek) {
        1 -> day = "Sunday"
        2 -> day = "Monday"
        3 -> day = "Tuesday"
        4 -> day = "Wednesday"
        5 -> day = "Thursday"
        6 -> day = "Friday"
        7 -> day = "Saturday"
    }
    return day
}

@BindingAdapter("imageResource")
fun setImageResource(imageview: ImageView, icon: String) {
    when (icon) {
        "01n" -> imageview.setImageDrawable(imageview, R.mipmap.ic_sunny)
        "02n",
        "03n",
        "04n" -> imageview.setImageDrawable(imageview, R.mipmap.ic_cloudy)
        "09n",
        "10n",
        "11n" -> imageview.setImageDrawable(imageview, R.mipmap.ic_rainy)
        "13n" -> imageview.setImageDrawable(imageview, R.drawable.ic_snow)
        "50n" -> imageview.setImageDrawable(imageview, R.drawable.ic_mist)
    }

}

private fun ImageView.setImageDrawable(view: ImageView, image: Int) {
    Glide.with(context).load(image).into(view)
}
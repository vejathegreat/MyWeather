package com.velaphi.myweather.ui.onboarding

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.navigation.fragment.findNavController
import com.velaphi.myweather.R
import com.velaphi.myweather.ui.MainActivity
import com.velaphi.myweather.utils.isConnected
import im.delight.android.location.SimpleLocation

class SplashFragment : Fragment() {

    var location: SimpleLocation? = null
    var latitude: String? = null
    var longitude: String? = null
    private var isLocationGranted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer()
    }

    private fun setupLocation() {
        location = SimpleLocation(requireContext())
        if (!location!!.hasLocationEnabled()) {
            SimpleLocation.openSettings(requireContext())
        } else {
            if (checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    REQUEST_CODE
                )
            } else {
                location = SimpleLocation(requireContext())
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                isLocationGranted = true
                navigateToNextScreen()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                location = SimpleLocation(context)
                latitude = String.format("%.6f", location?.latitude)
                longitude = String.format("%.6f", location?.longitude)
                isLocationGranted = true
            } else {
                isLocationGranted = false
            }
        }
        navigateToNextScreen()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun timer() {
        object : CountDownTimer(DURATION, INTERVAL) {

            override fun onFinish() {
                setupLocation()
            }

            override fun onTick(millisUntilFinished: Long) {

            }
        }.start()
    }

    private fun navigateToNextScreen() {

        if (!isConnected(context)) {
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToNetworkErrorFragment()
            )
        }else if (!isLocationGranted) {
            findNavController().navigate(
                SplashFragmentDirections.actionSplashFragmentToLocationErrorFragment()
            )
        } else{
            startActivity(MainActivity.getStartIntent(requireActivity(), latitude, longitude))
        }
    }


    companion object {
        const val REQUEST_CODE = 1
        const val DURATION = 2000L
        const val INTERVAL = 1000L
    }

//    private fun isConnected():Boolean{
//        val connectivityManager =
//            activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
//        val activeNetworkInfo = connectivityManager!!.activeNetworkInfo
//        return activeNetworkInfo != null && activeNetworkInfo.isConnected
//    }
}

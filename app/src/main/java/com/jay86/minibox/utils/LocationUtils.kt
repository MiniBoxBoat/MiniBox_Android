package com.jay86.minibox.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import com.amap.api.maps.model.LatLng
import com.jay86.minibox.App

/**
 * Created by Jay
 */
@SuppressLint("MissingPermission")
object LocationUtils : LocationListener {
    private var locationManager: LocationManager = App.context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private var locationProvider: String? = null

    var curLocation: Location? = null
        private set

    init {
        val providers = locationManager.getProviders(true)
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER
        }
        if (locationProvider != null) {
            curLocation = locationManager.getLastKnownLocation(locationProvider)
            locationManager.requestLocationUpdates(locationProvider, 3000, 1f, this)
        }
    }

    fun getCurLatLng() = if (curLocation != null) LatLng(curLocation!!.latitude, curLocation!!.longitude) else null

    override fun onLocationChanged(location: Location?) {
        curLocation = location
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
    }

    override fun onProviderEnabled(provider: String?) {
    }

    override fun onProviderDisabled(provider: String?) {
    }
}
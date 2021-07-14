package com.a2a.core.utility

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import java.io.IOException


class LocationHelper(context: Activity) {
    companion object {
        var REQUEST_CODE = 200
        const val RESULT_CODE = 201
        const val DURATION = 250
        const val ZOOM = 17F
    }

    var mFusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    var geocoder: Geocoder? = null
    var addresses: List<Address>? = null
    var permissionsEvents = MutableLiveData<PermissionsEvents>()

    fun getLocation(context: Activity) {
        var address: Address?
        var deviceLocation: Location?
        if (locationEnabled(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                if (REQUEST_CODE == 200)
                    permissionsEvents.value = PermissionsEvents.NeedPermission(REQUEST_CODE + 1)
            } else {
                mFusedLocationClient.lastLocation.addOnCompleteListener(context) { task ->
                    val location: Location? = task.result

                    deviceLocation = location
                    if (location != null) {
                        geocoder = Geocoder(context)
                        try {
                            if (geocoder != null) {
                                addresses =
                                    geocoder!!.getFromLocation(
                                        location.latitude,
                                        location.longitude,
                                        10
                                    )
                                address = (addresses as MutableList<Address>?)?.get(0)
                                permissionsEvents.value =
                                    PermissionsEvents.Granted(address?.countryCode, deviceLocation)

                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    } else
                        requestNewLocationData(context)
                }
            }
        } else {
            permissionsEvents.value = PermissionsEvents.NeedGPS
        }

    }

    private fun requestNewLocationData(context: Activity) {
        val mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1
        mFusedLocationClient =
            LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        )return
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
        getLocation(context)
    }

    private fun locationEnabled(context: Activity): Boolean {
        val locationManager: LocationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private var mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {

        }
    }

    fun requestPermissions(activity: Activity) {
        REQUEST_CODE++
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.requestPermissions(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), REQUEST_CODE
            )
        }
    }
}

sealed class PermissionsEvents {
    object NeverAskAgain : PermissionsEvents()
    object NeedGPS : PermissionsEvents()
    class Granted(val countryCode: String?, val location: Location?) : PermissionsEvents()
    class NeedPermission(val requestCode: Int) : PermissionsEvents()
}


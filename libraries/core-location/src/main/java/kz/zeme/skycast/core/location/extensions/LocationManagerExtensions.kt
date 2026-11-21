package kz.zeme.skycast.core.location.extensions

import android.location.LocationManager
import androidx.core.location.LocationManagerCompat

fun LocationManager.isLocationServiceEnabled(): Boolean =
    LocationManagerCompat.isLocationEnabled(this)

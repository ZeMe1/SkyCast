package com.example.skycast.core.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import androidx.core.content.ContextCompat
import com.example.skycast.core.domain.model.DeviceLocation
import com.example.skycast.core.domain.repository.LocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LocationRepositoryImpl(
    private val context: Context,
    private val fusedClient: FusedLocationProviderClient
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<DeviceLocation> {

        val hasFine = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        val hasCoarse = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED

        if (!hasFine && !hasCoarse) {
            return Result.failure(IllegalStateException("Location permission not granted"))
        }

        return runCatching {
            val location = getLastKnownLocation()
                ?: throw IllegalStateException("Location unavailable")
            val label = reverseGeocode(location.latitude, location.longitude)
            DeviceLocation(
                latitude = location.latitude,
                longitude = location.longitude,
                label = label
            )
        }
    }

    private suspend fun getLastKnownLocation(): Location? =
        suspendCancellableCoroutine { cont ->
            fusedClient.lastLocation
                .addOnSuccessListener { cont.resume(it) }
                .addOnFailureListener { cont.resumeWithException(it) }
        }

    private fun reverseGeocode(lat: Double, lon: Double): String {
        return runCatching {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(lat, lon, 1)
            val first = addresses?.firstOrNull()
            val city = first?.locality ?: first?.subAdminArea
            val country = first?.countryName
            when {
                city != null && country != null -> "$city, $country"
                city != null -> city
                else -> "${"%.4f".format(lat)}, ${"%.4f".format(lon)}"
            }
        }.getOrDefault("${"%.4f".format(lat)}, ${"%.4f".format(lon)}")
    }
}



package kz.zeme.skycast.core.location.data

import android.Manifest
import android.location.Location
import android.location.LocationManager
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.Task
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kz.zeme.skycast.core.activity.ActivityProvider
import kz.zeme.skycast.core.activity.extensions.isPermissionGranted
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.error.NotFoundError
import kz.zeme.skycast.core.location.domain.LocationPoint
import kz.zeme.skycast.core.location.domain.LocationRepository
import kz.zeme.skycast.core.location.extensions.isLocationServiceEnabled

@Suppress("SpreadOperator")
class DefaultLocationRepository(
    private val activityProvider: ActivityProvider,
    private val locationManager: LocationManager,
) : LocationRepository {

    companion object {
        private const val ALMATY_LATITUDE = 43.2389
        private const val ALMATY_LONGITUDE = 76.8897

        val DEFAULT_LOCATION = LocationPoint(
            latitude = ALMATY_LATITUDE,
            longitude = ALMATY_LONGITUDE,
        )
    }

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
    )

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ],
    )
    override suspend fun getCurrentLocation(): Response<LocationPoint> {
        val currentLocationResponse = requestCurrentLocation()
        if (currentLocationResponse is Response.Success) return currentLocationResponse

        val lastLocationResponse = getLastLocation()
        if (lastLocationResponse is Response.Success) return lastLocationResponse

        return Response.Success(DEFAULT_LOCATION)
    }

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ],
    )
    private suspend fun requestCurrentLocation(): Response<LocationPoint> =
        awaitLocation { client ->
            client.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        }

    @RequiresPermission(
        anyOf = [
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        ],
    )
    private suspend fun getLastLocation(): Response<LocationPoint> =
        awaitLocation { client ->
            client.lastLocation
        }

    private suspend fun awaitLocation(
        taskProvider: (FusedLocationProviderClient) -> Task<Location>,
    ): Response<LocationPoint> = suspendCoroutine { continuation ->
        val activity = activityProvider.currentActivity ?: run {
            continuation.resume(Response.Failure(NotFoundError()))
            return@suspendCoroutine
        }
        if (!activity.isPermissionGranted(*permissions)) {
            continuation.resume(Response.Failure(NotFoundError()))
            return@suspendCoroutine
        }
        if (locationManager.isLocationServiceEnabled().not()) {
            continuation.resume(Response.Failure(NotFoundError()))
            return@suspendCoroutine
        }

        val client = LocationServices.getFusedLocationProviderClient(activity)
        taskProvider(client)
            .addOnSuccessListener(activity) { location ->
                if (location == null) {
                    continuation.resume(Response.Failure(NotFoundError()))
                    return@addOnSuccessListener
                }

                val point = LocationPoint(location.latitude, location.longitude)
                continuation.resume(Response.Success(point))
            }
            .addOnFailureListener(activity) {
                continuation.resume(Response.Failure(NotFoundError()))
            }
            .addOnCanceledListener(activity) {
                continuation.resume(Response.Failure(NotFoundError()))
            }
    }
}

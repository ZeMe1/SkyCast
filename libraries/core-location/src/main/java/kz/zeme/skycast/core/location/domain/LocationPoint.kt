package kz.zeme.skycast.core.location.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LocationPoint(
    val latitude: Double,
    val longitude: Double,
) : Parcelable

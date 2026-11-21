package kz.zeme.skycast.core.location.domain

import kz.zeme.skycast.core.domain.Response

interface LocationRepository {

    suspend fun getCurrentLocation(): Response<LocationPoint>
}

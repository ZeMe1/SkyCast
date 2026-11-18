package kz.zeme.skycast.core.domain

import kz.zeme.skycast.core.error.BaseError

sealed class Response<T : Any> {
    data class Success<T : Any>(val data: T) : Response<T>()
    data class Failure<T : Any>(val data: BaseError) : Response<T>()
}

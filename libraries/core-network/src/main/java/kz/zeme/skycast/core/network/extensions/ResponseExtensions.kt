package kz.zeme.skycast.core.network.extensions

import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.observable.ViewState

fun <T : Any> Response<T>.extractValueOrNull(): T? = when (this) {
    is Response.Success -> data
    is Response.Failure -> null
}

fun <T : Any> Response<T>.extractValueOrDefault(default: T): T = when (this) {
    is Response.Success -> data
    is Response.Failure -> default
}

suspend fun <T : Any> Response<T>.getViewState(): ViewState<T> = getViewState(transform = { it })

suspend fun <X : Any, Y : Any> Response<X>.getViewState(
    transform: suspend (X) -> Y,
): ViewState<Y> = when (this) {
    is Response.Success -> ViewState.Data(transform(data))
    is Response.Failure -> ViewState.Error(data)
}

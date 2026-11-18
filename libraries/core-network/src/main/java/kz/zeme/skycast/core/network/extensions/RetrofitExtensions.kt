package kz.zeme.skycast.core.network.extensions

import kotlinx.coroutines.CancellationException
import kz.zeme.skycast.core.domain.Response
import kz.zeme.skycast.core.error.BaseError
import kz.zeme.skycast.core.error.UnexpectedError

suspend fun <Api : Any, Domain : Any> tryMapSuspended(
    request: suspend () -> retrofit2.Response<Api>,
    mapSuccess: suspend (Api) -> Domain,
    mapFailure: (retrofit2.Response<Api>) -> BaseError,
): Response<Domain> = try {
    request().map(
        mapSuccess = mapSuccess,
        mapFailure = mapFailure,
    )
} catch (ex: CancellationException) {
    throw ex
} catch (ex: Throwable) {
    Response.Failure(ex.toBaseError())
}

suspend fun <Api : Any> trySuspended(
    request: suspend () -> retrofit2.Response<Api>,
    mapFailure: (retrofit2.Response<Api>) -> BaseError,
): Response<Api> = tryMapSuspended(
    request = request,
    mapSuccess = { it },
    mapFailure = mapFailure,
)

suspend fun <Api : Any, Domain : Any> retrofit2.Response<Api>.map(
    mapSuccess: suspend (Api) -> Domain,
    mapFailure: (retrofit2.Response<Api>) -> BaseError,
): Response<Domain> {
    if (!isSuccessful) return Response.Failure(mapFailure(this))

    val body = body() ?: return Response.Failure(UnexpectedError(message = "Response body is null"))
    return Response.Success(mapSuccess(body))
}

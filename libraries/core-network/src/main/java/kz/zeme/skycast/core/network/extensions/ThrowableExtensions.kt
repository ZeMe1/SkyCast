package kz.zeme.skycast.core.network.extensions

import com.google.gson.JsonParseException
import kz.zeme.skycast.core.error.BaseError
import kz.zeme.skycast.core.error.ConnectionError
import kz.zeme.skycast.core.error.HttpError
import kz.zeme.skycast.core.error.NotFoundError
import kz.zeme.skycast.core.error.ParseError
import kz.zeme.skycast.core.error.ServerError
import kz.zeme.skycast.core.error.SslError
import kz.zeme.skycast.core.error.UnauthorizedError
import kz.zeme.skycast.core.error.UnexpectedError
import retrofit2.HttpException
import java.io.IOException
import javax.net.ssl.SSLException

fun Throwable.toBaseError(): BaseError = when (this) {
    is BaseError -> this
    is HttpException -> toBaseError(
        httpCode = code(),
        message = message(),
        underlying = this,
    )
    is JsonParseException -> ParseError(
        underlying = this,
        message = message,
    )
    is SSLException -> SslError(
        underlying = this,
        message = message,
    )
    is IOException -> ConnectionError(
        underlying = this,
        message = message,
    )
    else -> UnexpectedError(
        message = message,
        underlying = this,
    )
}

fun toBaseError(
    httpCode: Int,
    message: String?,
    underlying: Throwable? = null,
): BaseError = when (httpCode) {
    401 -> UnauthorizedError(
        message = message,
        underlying = underlying,
    )
    404 -> NotFoundError(
        message = message,
        underlying = underlying,
    )
    in 500..599 -> ServerError(
        serverCode = httpCode.toString(),
        message = message,
        underlying = underlying,
    )
    else -> HttpError(
        httpCode = httpCode,
        message = message,
        underlying = underlying,
    )
}

package kz.zeme.skycast.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class HttpError(
    val httpCode: Int = 0,
    override val code: ErrorCode = DefaultErrorCode,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)

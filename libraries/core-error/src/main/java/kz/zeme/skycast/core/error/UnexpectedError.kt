package kz.zeme.skycast.core.error

import kotlinx.parcelize.Parcelize

@Parcelize
data class UnexpectedError(
    override val code: ErrorCode = DefaultErrorCode,
    override val underlying: Throwable? = null,
    override val message: String? = null,
) : BaseError(code, underlying, message)

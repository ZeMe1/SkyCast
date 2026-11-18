package kz.zeme.skycast.core.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

abstract class BaseError(
    open val code: ErrorCode,
    open val underlying: Throwable? = null,
    override val message: String? = null,
) : Exception(message, underlying), Parcelable {

    interface ErrorCode : Parcelable

    @Parcelize
    data object DefaultErrorCode : ErrorCode

    open val name: String
        get() = this::class.simpleName.orEmpty()

    inline fun <reified T : BaseError> getUnderlyingErrorOrNull(): T? {
        var child = underlying as? BaseError
        while (child != null) {
            if (child is T) return child
            child = child.underlying as? BaseError
        }
        return null
    }
}

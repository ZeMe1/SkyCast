package kz.zeme.skycast.core.network.extensions

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kz.zeme.skycast.core.error.BaseError

data class ApiErrorResponse(
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("code")
    val code: String? = null,
)

fun Gson.mapFailure(response: retrofit2.Response<*>): BaseError {
    val rawErrorBody = response.errorBody()?.string()
    val parsedError = try {
        rawErrorBody?.let { fromJson(it, ApiErrorResponse::class.java) }
    } catch (ex: Exception) {
        null
    }

    val errorMessage = parsedError?.message
        .takeUnless { it.isNullOrBlank() }
        ?: response.message().takeUnless { it.isNullOrBlank() }

    return toBaseError(
        httpCode = response.code(),
        message = errorMessage,
        underlying = null,
    )
}

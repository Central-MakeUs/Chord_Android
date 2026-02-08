package com.team.chord.core.network.util

import com.team.chord.core.network.model.ApiError
import com.team.chord.core.network.model.ApiException
import com.team.chord.core.network.model.ApiResponse
import com.team.chord.core.network.model.UnauthorizedException
import kotlinx.serialization.json.Json
import retrofit2.Response

@Suppress("UNCHECKED_CAST")
suspend fun <T> safeApiCall(
    json: Json = Json { ignoreUnknownKeys = true },
    apiCall: suspend () -> Response<ApiResponse<T>>,
): T {
    val response = apiCall()
    if (response.isSuccessful) {
        val body = response.body()
            ?: throw ApiException(code = "UNKNOWN", message = "Empty response body")
        return body.data ?: (Unit as T)
    } else {
        val errorBody = response.errorBody()?.string()
        if (errorBody != null) {
            try {
                val apiError = json.decodeFromString<ApiError>(errorBody)
                throw ApiException(
                    code = apiError.code,
                    message = apiError.message,
                    errors = apiError.errors,
                )
            } catch (e: ApiException) {
                throw e
            } catch (_: Exception) {
                // Failed to parse error body, fall through
            }
        }
        if (response.code() == 401) {
            throw UnauthorizedException()
        }
        throw ApiException(
            code = "UNKNOWN",
            message = "Server error: ${response.code()}",
        )
    }
}

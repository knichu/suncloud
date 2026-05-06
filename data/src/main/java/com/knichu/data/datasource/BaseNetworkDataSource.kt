package com.knichu.data.datasource

import retrofit2.Response

abstract class BaseNetworkDataSource {

    /**
     * Retrofit Response<T>를 받아 성공 여부 확인 후 body 반환
     * 실패 시 예외 던짐
     */
    protected fun <T> checkResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body() ?: throw EmptyBodyException("Response body is null")
        } else {
            throw ApiException(
                code = response.code(),
                message = response.errorBody()?.string() ?: "Unknown API error"
            )
        }
    }

    // 예외 타입 정의
    class EmptyBodyException(message: String) : Exception(message)
    class ApiException(val code: Int, message: String) : Exception(message)
}
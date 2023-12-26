package com.knichu.data.datasource

import retrofit2.Response

abstract class BaseNetworkDataSource {
    protected fun <T> checkResponse(response: Response<T>): T {
        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception()
        }
    }
}

package com.knichu.data.service

import com.knichu.data.dto.request.ApiKeys
import com.knichu.data.dto.response.AirPollutionResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AirPollutionService {
    @GET("air_pollution")
    fun getAirPollution(
        @Query("lon") lon: Double? = null,
        @Query("lat") lat: Double? = null,
        @Query("appid") appid: String? = ApiKeys.OPEN_WEATHER_MAP_API_KEY
    ): Single<Response<AirPollutionResponseDTO>>
}
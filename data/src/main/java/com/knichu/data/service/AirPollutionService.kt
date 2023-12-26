package com.knichu.data.service

import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.data.dto.response.AirPollutionResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface AirPollutionService {
    @GET("air_pollution")
    fun getAirPollution(@Body airPollutionRequest: AirPollutionRequestDTO):
            Single<Response<AirPollutionResponseDTO>>
}
package com.knichu.data.service

import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.data.dto.response.AirPollutionResponseDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface AirPollutionService {
    @GET("air_pollution")
    suspend fun getAirPollution(@Body airPollutionRequest: AirPollutionRequestDTO):
            Response<AirPollutionResponseDTO>
}
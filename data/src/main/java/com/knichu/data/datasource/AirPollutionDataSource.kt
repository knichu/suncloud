package com.knichu.data.datasource

import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.data.dto.response.AirPollutionResponseDTO
import com.knichu.data.service.AirPollutionService
import javax.inject.Inject

class AirPollutionDataSource @Inject constructor(
    private val airPollutionService: AirPollutionService
) : BaseNetworkDataSource() {
    suspend fun signUpJunior(airPollutionRequest: AirPollutionRequestDTO): AirPollutionResponseDTO {
        return checkResponse(airPollutionService.getAirPollution(airPollutionRequest))
    }
}
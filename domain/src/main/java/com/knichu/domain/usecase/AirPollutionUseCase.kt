package com.knichu.domain.useCase

import com.knichu.domain.vo.AirPollutionDataVO

interface AirPollutionUseCase {
    suspend fun getCurrentPositionAirPollution(lon: Double, lat: Double): AirPollutionDataVO

    suspend fun getCityPositionAirPollution(city: String): AirPollutionDataVO
}

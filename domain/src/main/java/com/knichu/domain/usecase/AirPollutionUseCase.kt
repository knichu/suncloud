package com.knichu.domain.useCase

import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single

interface AirPollutionUseCase {
    fun getCurrentPositionAirPollution(lon: Double, lat: Double): Single<AirPollutionVO>

    fun getCityPositionAirPollution(city: String): Single<AirPollutionVO>
}
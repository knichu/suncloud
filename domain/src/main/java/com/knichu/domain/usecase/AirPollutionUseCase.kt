package com.knichu.domain.useCase

import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single

interface AirPollutionUseCase {
    fun getCurrentPositionAirPollution(lon: Double, lat: Double): Single<AirPollutionDataVO>

    fun getCityPositionAirPollution(city: String): Single<AirPollutionDataVO>
}
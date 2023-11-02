package com.knichu.domain.repository

import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single

interface AirPollutionRepository {
    fun getAirPollution(param: AirPollutionRequestParam): Result<Single<AirPollutionVO>>
}
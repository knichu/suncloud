package com.knichu.domain.repository

import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.vo.AirPollutionVO

interface AirPollutionRepository {
    fun getAirPollution(param: AirPollutionRequestParam): AirPollutionVO
}
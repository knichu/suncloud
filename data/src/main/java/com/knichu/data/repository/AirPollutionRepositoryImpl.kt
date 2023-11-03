package com.knichu.data.repository

import com.knichu.data.datasource.AirPollutionDataSource
import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AirPollutionRepositoryImpl @Inject constructor(
    private val airPollutionDataSource: AirPollutionDataSource
): AirPollutionRepository {
    override fun getAirPollution(param: AirPollutionRequestParam): Single<AirPollutionVO> {
        return airPollutionDataSource.getAirPollution(
            AirPollutionRequestDTO(
                lat = param.lat,
                lon = param.lon,
                appid = param.appid
            )
        )
    }
}
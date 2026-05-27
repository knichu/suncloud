package com.knichu.data.repository

import com.knichu.data.datasource.AirPollutionDataSource
import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.vo.AirPollutionVO
import javax.inject.Inject

class AirPollutionRepositoryImpl @Inject constructor(
    private val airPollutionDataSource: AirPollutionDataSource
) : AirPollutionRepository {

    override suspend fun getAirPollution(param: AirPollutionRequestParam): AirPollutionVO {
        return try {
            airPollutionDataSource.getAirPollution(
                AirPollutionRequestDTO(lon = param.lon, lat = param.lat)
            ).toDomain()
        } catch (e: Exception) {
            AirPollutionVO()
        }
    }
}

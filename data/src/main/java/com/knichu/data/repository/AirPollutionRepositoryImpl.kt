package com.knichu.data.repository

import com.knichu.data.datasource.AirPollutionDataSource
import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.vo.AirPollutionVO
import com.knichu.domain.vo.LiveWeatherVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AirPollutionRepositoryImpl @Inject constructor(
    private val airPollutionDataSource: AirPollutionDataSource
): AirPollutionRepository {
    override fun getAirPollution(param: AirPollutionRequestParam): Single<AirPollutionVO> {
        return airPollutionDataSource.getAirPollution(
            AirPollutionRequestDTO(
                lon = param.lon,
                lat = param.lat
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { AirPollutionVO() }
    }
}
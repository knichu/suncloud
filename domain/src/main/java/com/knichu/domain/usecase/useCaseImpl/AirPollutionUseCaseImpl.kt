package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.util.AirPollutionParser
import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.CityLocationVO
import javax.inject.Inject

class AirPollutionUseCaseImpl @Inject constructor(
    private val airPollutionRepository: AirPollutionRepository,
    private val cityLocationRepository: CityLocationRepository,
) : AirPollutionUseCase {

    @Volatile private var cachedCityLocationVO: CityLocationVO? = null

    private suspend fun getCityList(): CityLocationVO =
        cachedCityLocationVO ?: cityLocationRepository.getAllCityLocation().also { cachedCityLocationVO = it }

    override suspend fun getCurrentPositionAirPollution(lon: Double, lat: Double): AirPollutionDataVO {
        val airPollution = airPollutionRepository.getAirPollution(
            AirPollutionRequestParam(lon = lon, lat = lat)
        )
        return AirPollutionParser.getAirPollutionVO(airPollution)
    }

    override suspend fun getCityPositionAirPollution(city: String): AirPollutionDataVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val airPollution = airPollutionRepository.getAirPollution(
            AirPollutionRequestParam(
                lon = cityLocationItemVO?.longitude?.toDouble(),
                lat = cityLocationItemVO?.latitude?.toDouble()
            )
        )
        return AirPollutionParser.getAirPollutionVO(airPollution)
    }
}

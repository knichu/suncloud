package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.BuildConfig
import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.util.AirPollutionParser
import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AirPollutionUseCaseImpl @Inject constructor(
    private val airPollutionRepository: AirPollutionRepository,
    private val cityLocationRepository: CityLocationRepository,
): AirPollutionUseCase {
    private val allCityList = cityLocationRepository.getAllCityLocation()

    override fun getCurrentPositionAirPollution(lon: Double, lat: Double): Single<AirPollutionDataVO> {
        val airPollutionVO = airPollutionRepository.getAirPollution(
            AirPollutionRequestParam(
                lon = lon,
                lat = lat
            )
        )

        return AirPollutionParser.getAirPollutionVO(airPollutionVO)
    }

    override fun getCityPositionAirPollution(city: String): Single<AirPollutionDataVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val airPollutionVO = airPollutionRepository.getAirPollution(
                    AirPollutionRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble()
                    )
                )

                AirPollutionParser.getAirPollutionVO(airPollutionVO)
            }
    }
}
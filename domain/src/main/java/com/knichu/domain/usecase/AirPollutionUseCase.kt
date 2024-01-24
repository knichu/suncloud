package com.knichu.domain.usecase

import com.knichu.domain.BuildConfig
import com.knichu.domain.datastore.WeatherDataStore
import com.knichu.domain.param.AirPollutionRequestParam
import com.knichu.domain.repository.AirPollutionRepository
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class AirPollutionUseCase @Inject constructor(
    private val airPollutionRepository: AirPollutionRepository,
    private val cityLocationRepository: CityLocationRepository,
) {
    private val allCityList = cityLocationRepository.getAllCityLocation()
    private val openWeatherMapApiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY

    fun getCurrentPositionAirPollution(lon: Double, lat: Double): Single<AirPollutionVO> {
        return airPollutionRepository.getAirPollution(
            AirPollutionRequestParam(
                lon = lon,
                lat = lat,
                appid = openWeatherMapApiKey
            )
        )
    }

    fun getCityPositionAirPollution(city: String): Single<AirPollutionVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                airPollutionRepository.getAirPollution(
                    AirPollutionRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble(),
                        appid = openWeatherMapApiKey
                    )
                )
            }
    }
}
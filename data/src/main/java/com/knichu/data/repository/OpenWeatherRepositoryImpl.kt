package com.knichu.data.repository

import com.knichu.data.datasource.OpenWeatherDataSource
import com.knichu.data.dto.request.OpenWeatherRequestDTO
import com.knichu.domain.param.OpenWeatherRequestParam
import com.knichu.domain.repository.OpenWeatherRepository
import com.knichu.domain.vo.OpenWeatherVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val openWeatherDataSource: OpenWeatherDataSource
): OpenWeatherRepository {
    override fun getOpenWeather(param: OpenWeatherRequestParam): Single<OpenWeatherVO> {
        return openWeatherDataSource.getOpenWeather(
            OpenWeatherRequestDTO(
                lon = param.lon,
                lat = param.lat
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { OpenWeatherVO() }
    }
}
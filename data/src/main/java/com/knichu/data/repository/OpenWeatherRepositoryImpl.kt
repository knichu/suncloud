package com.knichu.data.repository

import com.knichu.data.datasource.OpenWeatherDataSource
import com.knichu.data.dto.request.OpenWeatherRequestDTO
import com.knichu.domain.param.OpenWeatherRequestParam
import com.knichu.domain.repository.OpenWeatherRepository
import com.knichu.domain.vo.OpenWeatherVO
import javax.inject.Inject

class OpenWeatherRepositoryImpl @Inject constructor(
    private val openWeatherDataSource: OpenWeatherDataSource
) : OpenWeatherRepository {

    override suspend fun getOpenWeather(param: OpenWeatherRequestParam): OpenWeatherVO {
        return try {
            openWeatherDataSource.getOpenWeather(
                OpenWeatherRequestDTO(lon = param.lon, lat = param.lat)
            ).toDomain()
        } catch (e: Exception) {
            OpenWeatherVO()
        }
    }
}

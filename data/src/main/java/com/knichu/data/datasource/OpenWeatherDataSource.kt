package com.knichu.data.datasource

import com.knichu.data.dto.request.OpenWeatherRequestDTO
import com.knichu.data.dto.response.OpenWeatherResponseDTO
import com.knichu.data.service.OpenWeatherService
import javax.inject.Inject

class OpenWeatherDataSource @Inject constructor(
    private val openWeatherService: OpenWeatherService
) : BaseNetworkDataSource() {

    suspend fun getOpenWeather(openWeatherRequest: OpenWeatherRequestDTO): OpenWeatherResponseDTO {
        return checkResponse(openWeatherService.getOpenWeather(
            lon = openWeatherRequest.lon,
            lat = openWeatherRequest.lat
        ))
    }
}

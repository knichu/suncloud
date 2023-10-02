package com.knichu.data.datasource

import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.data.dto.response.LiveWeatherResponseDTO
import com.knichu.data.dto.response.LongRainCloudResponseDTO
import com.knichu.data.dto.response.LongTemperatureResponseDTO
import com.knichu.data.dto.response.ShortWeatherResponseDTO
import com.knichu.data.dto.response.WeatherForecastTextResponseDTO
import com.knichu.data.service.WeatherService
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val weatherService: WeatherService
) : BaseNetworkDataSource() {
    suspend fun getLiveWeather(
        liveWeatherRequest: LiveWeatherRequestDTO
    ): LiveWeatherResponseDTO {
        return checkResponse(weatherService.getLiveWeather(liveWeatherRequest))
    }

    suspend fun getShortWeather(
        shortWeatherRequest: ShortWeatherRequestDTO
    ): ShortWeatherResponseDTO {
        return checkResponse(weatherService.getShortWeather(shortWeatherRequest))
    }

    suspend fun getLongRainCloud(
        longRainCloudRequest: LongRainCloudRequestDTO
    ): LongRainCloudResponseDTO {
        return checkResponse(weatherService.getLongRainCloud(longRainCloudRequest))
    }

    suspend fun getLongTemperature(
        longTemperatureRequest: LongTemperatureRequestDTO
    ): LongTemperatureResponseDTO {
        return checkResponse(weatherService.getLongTemperature(longTemperatureRequest))
    }

    suspend fun getWeatherForecastText(
        weatherForecastTextRequest: WeatherForecastTextRequestDTO
    ): WeatherForecastTextResponseDTO {
        return checkResponse(weatherService.getWeatherForecastText(weatherForecastTextRequest))
    }
}
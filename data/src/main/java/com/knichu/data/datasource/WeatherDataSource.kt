package com.knichu.data.datasource

import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.MidWeatherRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.data.dto.response.LiveWeatherResponseDTO
import com.knichu.data.dto.response.LongRainCloudResponseDTO
import com.knichu.data.dto.response.LongTemperatureResponseDTO
import com.knichu.data.dto.response.MidWeatherResponseDTO
import com.knichu.data.dto.response.ShortWeatherResponseDTO
import com.knichu.data.dto.response.WeatherForecastTextResponseDTO
import com.knichu.data.service.WeatherService
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val weatherService: WeatherService
) : BaseNetworkDataSource() {

    suspend fun getLiveWeather(liveWeatherRequest: LiveWeatherRequestDTO): LiveWeatherResponseDTO {
        return checkResponse(weatherService.getLiveWeather(
            baseDate = liveWeatherRequest.baseDate,
            baseTime = liveWeatherRequest.baseTime,
            nx = liveWeatherRequest.nx,
            ny = liveWeatherRequest.ny
        ))
    }

    suspend fun getShortWeather(shortWeatherRequest: ShortWeatherRequestDTO): ShortWeatherResponseDTO {
        return checkResponse(weatherService.getShortWeather(
            baseDate = shortWeatherRequest.baseDate,
            baseTime = shortWeatherRequest.baseTime,
            nx = shortWeatherRequest.nx,
            ny = shortWeatherRequest.ny
        ))
    }

    suspend fun getMidWeather(midWeatherRequest: MidWeatherRequestDTO): MidWeatherResponseDTO {
        return checkResponse(weatherService.getMidWeather(
            baseDate = midWeatherRequest.baseDate,
            nx = midWeatherRequest.nx,
            ny = midWeatherRequest.ny
        ))
    }

    suspend fun getLongRainCloud(longRainCloudRequest: LongRainCloudRequestDTO): LongRainCloudResponseDTO {
        return checkResponse(weatherService.getLongRainCloud(
            regId = longRainCloudRequest.regId,
            tmFc = longRainCloudRequest.tmFc
        ))
    }

    suspend fun getLongTemperature(longTemperatureRequest: LongTemperatureRequestDTO): LongTemperatureResponseDTO {
        return checkResponse(weatherService.getLongTemperature(
            regId = longTemperatureRequest.regId,
            tmFc = longTemperatureRequest.tmFc
        ))
    }

    suspend fun getWeatherForecastText(weatherForecastTextRequest: WeatherForecastTextRequestDTO): WeatherForecastTextResponseDTO {
        return checkResponse(weatherService.getWeatherForecastText(
            stnId = weatherForecastTextRequest.stnId,
            tmFc = weatherForecastTextRequest.tmFc
        ))
    }
}

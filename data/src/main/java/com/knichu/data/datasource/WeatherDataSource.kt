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
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val weatherService: WeatherService
) : BaseNetworkDataSource() {
    fun getLiveWeather(
        liveWeatherRequest: LiveWeatherRequestDTO
    ): Single<LiveWeatherResponseDTO> {
        return weatherService.getLiveWeather(liveWeatherRequest)
            .map { response -> checkResponse(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getShortWeather(
        shortWeatherRequest: ShortWeatherRequestDTO
    ): Single<ShortWeatherResponseDTO> {
        return weatherService.getShortWeather(shortWeatherRequest)
            .map { response -> checkResponse(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLongRainCloud(
        longRainCloudRequest: LongRainCloudRequestDTO
    ): Single<LongRainCloudResponseDTO> {
        return weatherService.getLongRainCloud(longRainCloudRequest)
            .map { response -> checkResponse(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLongTemperature(
        longTemperatureRequest: LongTemperatureRequestDTO
    ): Single<LongTemperatureResponseDTO> {
        return weatherService.getLongTemperature(longTemperatureRequest)
            .map { response -> checkResponse(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getWeatherForecastText(
        weatherForecastTextRequest: WeatherForecastTextRequestDTO
    ): Single<WeatherForecastTextResponseDTO> {
        return weatherService.getWeatherForecastText(weatherForecastTextRequest)
            .map { response -> checkResponse(response) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
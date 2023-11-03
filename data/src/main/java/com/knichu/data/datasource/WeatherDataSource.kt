package com.knichu.data.datasource

import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.data.service.WeatherService
import com.knichu.domain.vo.AirPollutionVO
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherForecastTextVO
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class WeatherDataSource @Inject constructor(
    private val weatherService: WeatherService
) : BaseNetworkDataSource() {
    fun getLiveWeather(
        liveWeatherRequest: LiveWeatherRequestDTO
    ): Single<LiveWeatherVO> {
        return weatherService.getLiveWeather(liveWeatherRequest)
            .map { response -> checkResponse(response) }
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { LiveWeatherVO() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getShortWeather(
        shortWeatherRequest: ShortWeatherRequestDTO
    ): Single<ShortWeatherVO> {
        return weatherService.getShortWeather(shortWeatherRequest)
            .map { response -> checkResponse(response) }
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { ShortWeatherVO() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLongRainCloud(
        longRainCloudRequest: LongRainCloudRequestDTO
    ): Single<LongRainCloudVO> {
        return weatherService.getLongRainCloud(longRainCloudRequest)
            .map { response -> checkResponse(response) }
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { LongRainCloudVO() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getLongTemperature(
        longTemperatureRequest: LongTemperatureRequestDTO
    ): Single<LongTemperatureVO> {
        return weatherService.getLongTemperature(longTemperatureRequest)
            .map { response -> checkResponse(response) }
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { LongTemperatureVO() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun getWeatherForecastText(
        weatherForecastTextRequest: WeatherForecastTextRequestDTO
    ): Single<WeatherForecastTextVO> {
        return weatherService.getWeatherForecastText(weatherForecastTextRequest)
            .map { response -> checkResponse(response) }
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { WeatherForecastTextVO() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }
}
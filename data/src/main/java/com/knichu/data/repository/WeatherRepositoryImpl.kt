package com.knichu.data.repository

import com.knichu.data.datasource.WeatherDataSource
import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.MidWeatherRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.MidWeatherRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.repository.WeatherRepository
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.MidWeatherVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherForecastTextVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
): WeatherRepository {

    override fun getLiveWeather(param: LiveWeatherRequestParam): Single<LiveWeatherVO> {
        return weatherDataSource.getLiveWeather(
            LiveWeatherRequestDTO(
                baseDate = param.baseDate,
                baseTime = param.baseTime,
                nx = param.nx,
                ny = param.ny
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { LiveWeatherVO(item = emptyList()) }
    }

    override fun getShortWeather(param: ShortWeatherRequestParam): Single<ShortWeatherVO> {
        return weatherDataSource.getShortWeather(
            ShortWeatherRequestDTO(
                baseDate = param.baseDate,
                baseTime = param.baseTime,
                nx = param.nx,
                ny = param.ny
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { ShortWeatherVO(item = emptyList()) }
    }

    override fun getMidWeather(param: MidWeatherRequestParam): Single<MidWeatherVO> {
        return weatherDataSource.getMidWeather(
            MidWeatherRequestDTO(
                baseDate = param.baseDate,
                nx = param.nx,
                ny = param.ny
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { MidWeatherVO(item = emptyList()) }
    }

    override fun getLongRainCloud(param: LongRainCloudRequestParam): Single<LongRainCloudVO> {
        return weatherDataSource.getLongRainCloud(
            LongRainCloudRequestDTO(
                regId = param.regId,
                tmFc = param.tmFc
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { LongRainCloudVO() }
    }

    override fun getLongTemperature(param: LongTemperatureRequestParam): Single<LongTemperatureVO> {
        return weatherDataSource.getLongTemperature(
            LongTemperatureRequestDTO(
                regId = param.regId,
                tmFc = param.tmFc
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { LongTemperatureVO() }
    }

    override fun getWeatherForecastText(param: WeatherForecastTextRequestParam): Single<WeatherForecastTextVO> {
        return weatherDataSource.getWeatherForecastText(
            WeatherForecastTextRequestDTO(
                stnId = param.stnId,
                tmFc = param.tmFc
            )
        )
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { WeatherForecastTextVO() }
    }
}
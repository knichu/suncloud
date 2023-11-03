package com.knichu.data.repository

import com.knichu.data.datasource.WeatherDataSource
import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.repository.WeatherRepository
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherForecastTextVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
): WeatherRepository {

    override fun getLiveWeather(param: LiveWeatherRequestParam): Single<LiveWeatherVO> {
        return weatherDataSource.getLiveWeather(
            LiveWeatherRequestDTO(
                serviceKey = param.serviceKey,
                pageNo = param.pageNo,
                numOfRows = param.numOfRows,
                dataType = param.dataType,
                baseDate = param.baseDate,
                baseTime = param.baseTime,
                nx = param.nx,
                ny = param.ny
            )
        )
    }

    override fun getShortWeather(param: ShortWeatherRequestParam): Single<ShortWeatherVO> {
        return weatherDataSource.getShortWeather(
            ShortWeatherRequestDTO(
                serviceKey = param.serviceKey,
                pageNo = param.pageNo,
                numOfRows = param.numOfRows,
                dataType = param.dataType,
                baseDate = param.baseDate,
                baseTime = param.baseTime,
                nx = param.nx,
                ny = param.ny
            )
        )
    }

    override fun getLongRainCloud(param: LongRainCloudRequestParam): Single<LongRainCloudVO> {
        return weatherDataSource.getLongRainCloud(
            LongRainCloudRequestDTO(
                serviceKey = param.serviceKey,
                pageNo = param.pageNo,
                numOfRows = param.numOfRows,
                dataType = param.dataType,
                regId = param.regId,
                tmFc = param.tmFc
            )
        )
    }

    override fun getLongTemperature(param: LongTemperatureRequestParam): Single<LongTemperatureVO> {
        return weatherDataSource.getLongTemperature(
            LongTemperatureRequestDTO(
                serviceKey = param.serviceKey,
                pageNo = param.pageNo,
                numOfRows = param.numOfRows,
                dataType = param.dataType,
                regId = param.regId,
                tmFc = param.tmFc
            )
        )
    }

    override fun getWeatherForecastText(param: WeatherForecastTextRequestParam): Single<WeatherForecastTextVO> {
        return weatherDataSource.getWeatherForecastText(
            WeatherForecastTextRequestDTO(
                serviceKey = param.serviceKey,
                pageNo = param.pageNo,
                numOfRows = param.numOfRows,
                dataType = param.dataType,
                stnId = param.stnId,
                tmFc = param.tmFc
            )
        )
    }
}
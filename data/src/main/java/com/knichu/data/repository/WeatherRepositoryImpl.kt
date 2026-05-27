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
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherDataSource: WeatherDataSource
) : WeatherRepository {

    override suspend fun getLiveWeather(param: LiveWeatherRequestParam): LiveWeatherVO {
        return try {
            weatherDataSource.getLiveWeather(
                LiveWeatherRequestDTO(baseDate = param.baseDate, baseTime = param.baseTime, nx = param.nx, ny = param.ny)
            ).toDomain()
        } catch (e: Exception) {
            LiveWeatherVO(item = emptyList())
        }
    }

    override suspend fun getShortWeather(param: ShortWeatherRequestParam): ShortWeatherVO {
        return try {
            weatherDataSource.getShortWeather(
                ShortWeatherRequestDTO(baseDate = param.baseDate, baseTime = param.baseTime, nx = param.nx, ny = param.ny)
            ).toDomain()
        } catch (e: Exception) {
            ShortWeatherVO(item = emptyList())
        }
    }

    override suspend fun getMidWeather(param: MidWeatherRequestParam): MidWeatherVO {
        return try {
            weatherDataSource.getMidWeather(
                MidWeatherRequestDTO(baseDate = param.baseDate, nx = param.nx, ny = param.ny)
            ).toDomain()
        } catch (e: Exception) {
            MidWeatherVO(item = emptyList())
        }
    }

    override suspend fun getLongRainCloud(param: LongRainCloudRequestParam): LongRainCloudVO {
        return try {
            weatherDataSource.getLongRainCloud(
                LongRainCloudRequestDTO(regId = param.regId, tmFc = param.tmFc)
            ).toDomain()
        } catch (e: Exception) {
            LongRainCloudVO()
        }
    }

    override suspend fun getLongTemperature(param: LongTemperatureRequestParam): LongTemperatureVO {
        return try {
            weatherDataSource.getLongTemperature(
                LongTemperatureRequestDTO(regId = param.regId, tmFc = param.tmFc)
            ).toDomain()
        } catch (e: Exception) {
            LongTemperatureVO()
        }
    }

    override suspend fun getWeatherForecastText(param: WeatherForecastTextRequestParam): WeatherForecastTextVO {
        return try {
            weatherDataSource.getWeatherForecastText(
                WeatherForecastTextRequestDTO(stnId = param.stnId, tmFc = param.tmFc)
            ).toDomain()
        } catch (e: Exception) {
            WeatherForecastTextVO()
        }
    }
}

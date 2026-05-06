package com.knichu.domain.repository

import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.MidWeatherRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.MidWeatherVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherForecastTextVO
interface WeatherRepository {
    suspend fun getLiveWeather(param: LiveWeatherRequestParam): LiveWeatherVO
    suspend fun getShortWeather(param: ShortWeatherRequestParam): ShortWeatherVO
    suspend fun getMidWeather(param: MidWeatherRequestParam): MidWeatherVO
    suspend fun getLongRainCloud(param: LongRainCloudRequestParam): LongRainCloudVO
    suspend fun getLongTemperature(param: LongTemperatureRequestParam): LongTemperatureVO
    suspend fun getWeatherForecastText(param: WeatherForecastTextRequestParam): WeatherForecastTextVO
}
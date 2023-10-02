package com.knichu.domain.repository

import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherForecastTextVO

interface WeatherRepository {
    fun getLiveWeather(param: LiveWeatherRequestParam): LiveWeatherVO

    fun getShortWeather(param: ShortWeatherRequestParam): ShortWeatherVO

    fun getLongRainCloud(param: LongRainCloudRequestParam): LongRainCloudVO

    fun getLongTemperature(param: LongTemperatureRequestParam): LongTemperatureVO

    fun getWeatherForecastText(param: WeatherForecastTextRequestParam): WeatherForecastTextVO
}
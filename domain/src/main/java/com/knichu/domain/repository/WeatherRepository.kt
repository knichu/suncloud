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
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getLiveWeather(param: LiveWeatherRequestParam): Single<LiveWeatherVO>
    fun getShortWeather(param: ShortWeatherRequestParam): Single<ShortWeatherVO>
    fun getLongRainCloud(param: LongRainCloudRequestParam): Single<LongRainCloudVO>
    fun getLongTemperature(param: LongTemperatureRequestParam): Single<LongTemperatureVO>
    fun getWeatherForecastText(param: WeatherForecastTextRequestParam): Single<WeatherForecastTextVO>
}
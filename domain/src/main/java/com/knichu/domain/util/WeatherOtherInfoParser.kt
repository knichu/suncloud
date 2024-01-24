package com.knichu.domain.util

import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import io.reactivex.rxjava3.core.Single

class WeatherOtherInfoParser {

    fun getWeatherOtherInfoVO(liveWeatherVO: Single<LiveWeatherVO>): Single<WeatherOtherInfoVO> {
        return liveWeatherVO.map { liveWeather ->
            val humidity = liveWeather.item?.find { it.category == "REH" }?.observeValue
            val windDirection = liveWeather.item?.find { it.category == "VEC" }?.observeValue
            val windSpeed = liveWeather.item?.find { it.category == "WSD" }?.observeValue

            WeatherOtherInfoVO(
                humidity = humidity,
                windDirection = windDirection,
                windSpeed = windSpeed,
            )
        }
    }
}
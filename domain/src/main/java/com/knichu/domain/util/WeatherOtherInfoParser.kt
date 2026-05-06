package com.knichu.domain.util

import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.WeatherOtherInfoVO

object WeatherOtherInfoParser {

    fun getWeatherOtherInfoVO(liveWeather: LiveWeatherVO): WeatherOtherInfoVO {
        val humidity = liveWeather.item?.find { it.category == "REH" }?.observeValue
        val windDirection = liveWeather.item?.find { it.category == "VEC" }?.observeValue
        val windSpeed = liveWeather.item?.find { it.category == "WSD" }?.observeValue
        val convertedWindDirectionValue =
            windDirection?.toInt()?.plus(22.5)?.div(45)?.toInt().toString()
        return WeatherOtherInfoVO(
            humidity = humidity,
            windDirection = convertedWindDirectionValue,
            windSpeed = windSpeed,
        )
    }
}

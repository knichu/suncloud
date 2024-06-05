package com.knichu.domain.util

import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

object WeatherOtherInfoParser {

    fun getWeatherOtherInfoVO(liveWeatherVO: Single<LiveWeatherVO>): Single<WeatherOtherInfoVO> {
        return liveWeatherVO.map { liveWeather ->
            val humidity = liveWeather.item?.find { it.category == "REH" }?.observeValue
            val windDirection = liveWeather.item?.find { it.category == "VEC" }?.observeValue
            val windSpeed = liveWeather.item?.find { it.category == "WSD" }?.observeValue

            val convertedWindDirectionValue =
                windDirection?.toInt()?.plus(22.5)?.div(45)?.toInt().toString()

            WeatherOtherInfoVO(
                humidity = humidity,
                windDirection = convertedWindDirectionValue,
                windSpeed = windSpeed,
            )
        }
            .observeOn(Schedulers.computation())
    }
}
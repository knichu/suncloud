package com.knichu.domain.util

import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.WeatherNowVO
import io.reactivex.rxjava3.core.Single

class WeatherNowParser {

    fun getWeatherNowVO(
        liveWeatherVO: Single<LiveWeatherVO>,
        currentPositionCity: Single<String>
    ): Single<WeatherNowVO> {
        return Single.zip(
            liveWeatherVO,
            currentPositionCity
        ) { liveWeather, city ->
            val temperature = liveWeather.item?.find { it.category == "T1H" }?.observeValue
            val skyItem = liveWeather.item?.find { it.category == "SKY" }
            val ptyItem = liveWeather.item?.find { it.category == "PTY" }
            val weatherCondition = when (skyItem?.observeValue) {
                "1" -> "1"
                "3" -> when (ptyItem?.observeValue) {
                    "0" -> "2"
                    "1" -> "3"
                    "2" -> "5"
                    "3" -> "4"
                    "4" -> "6"
                    "5" -> "3"
                    "6" -> "5"
                    "7" -> "4"
                    else -> null
                }
                "4" -> when (ptyItem?.observeValue) {
                    "0" -> "7"
                    "1" -> "8"
                    "2" -> "10"
                    "3" -> "9"
                    "4" -> "11"
                    "5" -> "8"
                    "6" -> "10"
                    "7" -> "9"
                    else -> null
                }
                else -> null
            }
            WeatherNowVO(
                temperature = temperature,
                city = city,
                weatherCondition = weatherCondition,
            )
        }
    }

    fun getWeatherNowVO(
        liveWeatherVO: Single<LiveWeatherVO>,
        currentPositionCity: String?
    ): Single<WeatherNowVO> {
        return liveWeatherVO.map { liveWeather ->
            val temperature = liveWeather.item?.find { it.category == "T1H" }?.observeValue
            val skyItem = liveWeather.item?.find { it.category == "SKY" }
            val ptyItem = liveWeather.item?.find { it.category == "PTY" }
            val weatherCondition = when (skyItem?.observeValue) {
                "1" -> "1"
                "3" -> when (ptyItem?.observeValue) {
                    "0" -> "2"
                    "1" -> "3"
                    "2" -> "5"
                    "3" -> "4"
                    "4" -> "6"
                    "5" -> "3"
                    "6" -> "5"
                    "7" -> "4"
                    else -> null
                }
                "4" -> when (ptyItem?.observeValue) {
                    "0" -> "7"
                    "1" -> "8"
                    "2" -> "10"
                    "3" -> "9"
                    "4" -> "11"
                    "5" -> "8"
                    "6" -> "10"
                    "7" -> "9"
                    else -> null
                }
                else -> null
            }
            WeatherNowVO(
                temperature = temperature,
                city = currentPositionCity,
                weatherCondition = weatherCondition,
            )
        }
    }

}
package com.knichu.domain.util

import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.Weather24HourVO
import io.reactivex.rxjava3.core.Single

class Weather24HourParser {

    fun getWeather24HourVO(
        shortWeatherVO: Single<ShortWeatherVO>
    ): Single<Weather24HourVO> {
        return shortWeatherVO
            .map { shortWeather ->
            val weather24HourItemList = mutableListOf<Weather24HourItemVO>()
            var tempShortWeatherList = mutableListOf<ShortWeatherItemVO>()
            var timeIndex = shortWeather.item?.firstOrNull()?.forecastTime
            var startIndex = 0
            for (item in shortWeather.item!!) {
                if (startIndex >= 24) {
                    break
                }
                if (item.forecastTime == timeIndex) {
                    tempShortWeatherList.add(item)
                } else {
                    val weatherCondition = when (tempShortWeatherList.find { it.category == "SKY" }?.forecastValue) {
                        "1" -> "1"
                        "3" -> {
                            when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                                "0" -> "2"
                                "1" -> "3"
                                "2" -> "5"
                                "3" -> "4"
                                "4" -> "6"
                                else -> null
                            }
                        }
                        "4" -> {
                            when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                                "0" -> "7"
                                "1" -> "8"
                                "2" -> "10"
                                "3" -> "9"
                                "4" -> "11"
                                else -> null
                            }
                        }
                        else -> null
                    }

                    val temperature = tempShortWeatherList.find { it.category == "TMP" }?.forecastValue
                    val rainProbability = tempShortWeatherList.find { it.category == "POP" }?.forecastValue

                    val weather24HourItem = Weather24HourItemVO(
                        time = timeIndex,
                        weatherCondition = weatherCondition,
                        temperature = temperature,
                        rainProbability = rainProbability
                    )

                    weather24HourItemList.add(weather24HourItem)

                    startIndex += 1
                    timeIndex = item.forecastTime
                    tempShortWeatherList = mutableListOf(item)
                }
            }
            Weather24HourVO(weather24HourItemList)
        }
    }
}
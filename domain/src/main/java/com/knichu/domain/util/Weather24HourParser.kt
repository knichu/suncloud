package com.knichu.domain.util

import com.knichu.domain.util.DateTimeParser.UnixToHHdd
import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.Weather24HourVO

object Weather24HourParser {

    fun getWeather24HourVO(
        shortWeather: ShortWeatherVO,
        openWeather: OpenWeatherVO,
        checkTime: String
    ): Weather24HourVO {
        val weather24HourItemList = mutableListOf<Weather24HourItemVO>()
        var tempShortWeatherList = mutableListOf<ShortWeatherItemVO>()
        var timeIndex = shortWeather.item?.firstOrNull()?.forecastTime
        val sunriseTime = UnixToHHdd(openWeather.sunrise, true)
        val sunsetTime = UnixToHHdd(openWeather.sunset, false)
        var startIndex = 0
        for (item in shortWeather.item!!) {
            if (startIndex >= 24) {
                break
            }
            if (item.forecastTime == timeIndex) {
                tempShortWeatherList.add(item)
            } else {
                val weatherCondition = when (tempShortWeatherList.find { it.category == "SKY" }?.forecastValue) {
                    "1" -> if (timeIndex?.toInt() in sunriseTime..sunsetTime) "1" else "12"
                    "3" -> when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                        "0" -> if (timeIndex?.toInt() in sunriseTime..sunsetTime) "2" else "13"
                        "1" -> "3"
                        "2" -> "5"
                        "3" -> "4"
                        "4" -> "6"
                        else -> null
                    }
                    "4" -> when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                        "0" -> if (timeIndex?.toInt() in sunriseTime..sunsetTime) "7" else "14"
                        "1" -> "8"
                        "2" -> "10"
                        "3" -> "9"
                        "4" -> "11"
                        else -> null
                    }
                    else -> null
                }
                val temperature = tempShortWeatherList.find { it.category == "TMP" }?.forecastValue
                val rainProbability = tempShortWeatherList.find { it.category == "POP" }?.forecastValue
                weather24HourItemList.add(
                    Weather24HourItemVO(
                        time = timeIndex,
                        weatherCondition = weatherCondition,
                        temperature = temperature,
                        rainProbability = rainProbability
                    )
                )
                startIndex += 1
                timeIndex = item.forecastTime
                tempShortWeatherList = mutableListOf(item)
            }
        }
        return Weather24HourVO(weather24HourItemList)
    }
}

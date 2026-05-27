package com.knichu.domain.util

import com.knichu.domain.util.DateTimeParser.UnixToHHdd
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO

object WeatherNowParser {

    fun getWeatherNowVO(
        liveWeather: LiveWeatherVO,
        shortWeather: ShortWeatherVO,
        openWeather: OpenWeatherVO,
        city: String?
    ): WeatherNowVO {
        val temperature = liveWeather.item?.find { it.category == "T1H" }?.observeValue
        val weatherCondition = calculateWeatherCondition(shortWeather, openWeather)
        return WeatherNowVO(
            temperature = temperature,
            city = city,
            weatherCondition = weatherCondition,
        )
    }

    fun getWeatherNowCityListItemVO(
        liveWeather: LiveWeatherVO,
        shortWeather: ShortWeatherVO,
        openWeather: OpenWeatherVO,
        city: String?
    ): WeatherNowCityListItemVO {
        val temperature = liveWeather.item?.find { it.category == "T1H" }?.observeValue
        val weatherCondition = calculateWeatherCondition(shortWeather, openWeather)
        return WeatherNowCityListItemVO(
            temperature = temperature,
            city = city,
            weatherCondition = weatherCondition,
        )
    }

    private fun calculateWeatherCondition(
        shortWeather: ShortWeatherVO,
        openWeather: OpenWeatherVO
    ): String? {
        var weatherCondition: String? = ""
        var tempShortWeatherList = mutableListOf<ShortWeatherItemVO>()
        val timeIndex = shortWeather.item?.firstOrNull()?.forecastTime
        val sunriseTime = UnixToHHdd(openWeather.sunrise, true)
        val sunsetTime = UnixToHHdd(openWeather.sunset, false)
        for (item in shortWeather.item!!) {
            if (item.forecastTime == timeIndex) {
                tempShortWeatherList.add(item)
            } else {
                weatherCondition = when (tempShortWeatherList.find { it.category == "SKY" }?.forecastValue) {
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
            }
        }
        return weatherCondition
    }
}

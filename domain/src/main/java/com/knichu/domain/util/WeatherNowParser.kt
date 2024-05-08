package com.knichu.domain.util

import com.knichu.domain.util.DateTimeParser.UnixToHHdd
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherNowVO
import io.reactivex.rxjava3.core.Single

object WeatherNowParser {

    fun getWeatherNowVO(
        liveWeatherVO: Single<LiveWeatherVO>,
        shortWeatherVO: Single<ShortWeatherVO>,
        openWeatherVO: Single<OpenWeatherVO>,
        currentPositionCity: Single<String>
    ): Single<WeatherNowVO> {
        return Single.zip(
            liveWeatherVO,
            shortWeatherVO,
            openWeatherVO,
            currentPositionCity
        ) { liveWeather, shortWeather, openWeather, city ->
            val temperature = liveWeather.item?.find { it.category == "T1H" }?.observeValue

            var weatherCondition: String? = ""
            var tempShortWeatherList = mutableListOf<ShortWeatherItemVO>()
            val timeIndex = shortWeather.item?.firstOrNull()?.forecastTime
            val sunriseTime = UnixToHHdd(openWeather.sunrise, true)
            val sunsetTime = UnixToHHdd(openWeather.sunset, false)
            for (item in shortWeather.item!!) {
                if (item.forecastTime == timeIndex) {
                    tempShortWeatherList.add(item)
                } else {
                    val tempWeatherCondition = when (tempShortWeatherList.find { it.category == "SKY" }?.forecastValue) {
                        "1" -> {
                            when (timeIndex?.toInt()) {
                                in sunriseTime .. sunsetTime -> "1"
                                else -> "12"
                            }
                        }
                        "3" -> {
                            when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                                "0" -> {
                                    when (timeIndex?.toInt()) {
                                        in sunriseTime .. sunsetTime -> "2"
                                        else -> "13"
                                    }
                                }
                                "1" -> "3"
                                "2" -> "5"
                                "3" -> "4"
                                "4" -> "6"
                                else -> null
                            }
                        }
                        "4" -> {
                            when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                                "0" -> {
                                    when (timeIndex?.toInt()) {
                                        in sunriseTime .. sunsetTime -> "7"
                                        else -> "14"
                                    }
                                }
                                "1" -> "8"
                                "2" -> "10"
                                "3" -> "9"
                                "4" -> "11"
                                else -> null
                            }
                        }
                        else -> null
                    }
                    weatherCondition = tempWeatherCondition
                }
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
        shortWeatherVO: Single<ShortWeatherVO>,
        openWeatherVO: Single<OpenWeatherVO>,
        currentPositionCity: String?
    ): Single<WeatherNowVO> {
        return Single.zip(
            liveWeatherVO,
            shortWeatherVO,
            openWeatherVO
        ) { liveWeather, shortWeather, openWeather ->
            val temperature = liveWeather.item?.find { it.category == "T1H" }?.observeValue

            var weatherCondition: String? = ""
            var tempShortWeatherList = mutableListOf<ShortWeatherItemVO>()
            val timeIndex = shortWeather.item?.firstOrNull()?.forecastTime
            val sunriseTime = UnixToHHdd(openWeather.sunrise, true)
            val sunsetTime = UnixToHHdd(openWeather.sunset, false)
            for (item in shortWeather.item!!) {
                if (item.forecastTime == timeIndex) {
                    tempShortWeatherList.add(item)
                } else {
                    val tempWeatherCondition = when (tempShortWeatherList.find { it.category == "SKY" }?.forecastValue) {
                        "1" -> {
                            when (timeIndex?.toInt()) {
                                in sunriseTime .. sunsetTime -> "1"
                                else -> "12"
                            }
                        }
                        "3" -> {
                            when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                                "0" -> {
                                    when (timeIndex?.toInt()) {
                                        in sunriseTime .. sunsetTime -> "2"
                                        else -> "13"
                                    }
                                }
                                "1" -> "3"
                                "2" -> "5"
                                "3" -> "4"
                                "4" -> "6"
                                else -> null
                            }
                        }
                        "4" -> {
                            when (tempShortWeatherList.find { it.category == "PTY" }?.forecastValue) {
                                "0" -> {
                                    when (timeIndex?.toInt()) {
                                        in sunriseTime .. sunsetTime -> "7"
                                        else -> "14"
                                    }
                                }
                                "1" -> "8"
                                "2" -> "10"
                                "3" -> "9"
                                "4" -> "11"
                                else -> null
                            }
                        }
                        else -> null
                    }
                    weatherCondition = tempWeatherCondition
                }
            }


            WeatherNowVO(
                temperature = temperature,
                city = currentPositionCity,
                weatherCondition = weatherCondition,
            )
        }
    }

}
package com.knichu.domain.util

import com.knichu.domain.util.DateTimeParser.UnixToHHdd
import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.Weather24HourVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object Weather24HourParser {

    fun getWeather24HourVO(
        shortWeatherVO: Single<ShortWeatherVO>,
        openWeatherVO: Single<OpenWeatherVO>
    ): Single<Weather24HourVO> {
        return Single.zip(
            shortWeatherVO,
            openWeatherVO
        ) { shortWeather, openWeather ->
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
            .observeOn(Schedulers.computation())
    }

}
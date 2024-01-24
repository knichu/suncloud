package com.knichu.domain.util

import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.SunriseSunsetVO
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SunriseSunsetParser {

    fun getSunriseSunsetVO(
        openWeatherVO: Single<OpenWeatherVO>
    ): Single<SunriseSunsetVO> {
        return openWeatherVO
            .map{ openWeather ->
                val sunriseUTC = convertUnixTimeToFormattedString(openWeather.sunrise?.toLong() ?: 0)
                val sunsetUTC = convertUnixTimeToFormattedString(openWeather.sunset?.toLong() ?: 0)
                SunriseSunsetVO(
                    sunriseTime = sunriseUTC.toInt(),
                    sunsetTime = sunsetUTC.toInt()
                )
            }
    }

    private fun convertUnixTimeToFormattedString(unixTime: Long): String {
        val dateFormat = SimpleDateFormat("HHmm", Locale.getDefault())
        dateFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
        val date = Date(unixTime * 1000)
        return dateFormat.format(date)
    }
}
package com.knichu.domain.util

import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.SunriseSunsetVO
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object SunriseSunsetParser {

    fun getSunriseSunsetVO(openWeather: OpenWeatherVO): SunriseSunsetVO {
        return SunriseSunsetVO(
            sunriseTime = convertUnixTimeToFormattedString(openWeather.sunrise?.toLong() ?: 0),
            sunsetTime = convertUnixTimeToFormattedString(openWeather.sunset?.toLong() ?: 0)
        )
    }

    private fun convertUnixTimeToFormattedString(unixTime: Long): String {
        val dateFormat = SimpleDateFormat("HHmm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+9:00")
        val date = Date(unixTime * 1000)
        return dateFormat.format(date)
    }
}

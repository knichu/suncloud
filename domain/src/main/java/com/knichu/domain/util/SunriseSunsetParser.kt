package com.knichu.domain.util

import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.SunriseSunsetVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object SunriseSunsetParser {

    fun getSunriseSunsetVO(
        openWeatherVO: Single<OpenWeatherVO>
    ): Single<SunriseSunsetVO> {
        return openWeatherVO
            .map{ openWeather ->
                val sunriseUTC = convertUnixTimeToFormattedString(openWeather.sunrise?.toLong() ?: 0)
                val sunsetUTC = convertUnixTimeToFormattedString(openWeather.sunset?.toLong() ?: 0)
                SunriseSunsetVO(
                    sunriseTime = sunriseUTC,
                    sunsetTime = sunsetUTC
                )
            }
            .observeOn(Schedulers.computation())
    }

    private fun convertUnixTimeToFormattedString(unixTime: Long): String {
        val dateFormat = SimpleDateFormat("HHmm", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("GMT+9:00")
        val date = Date(unixTime * 1000)
        return dateFormat.format(date)
    }
}
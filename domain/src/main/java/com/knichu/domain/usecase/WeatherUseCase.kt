package com.knichu.domain.useCase

import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyVO
import io.reactivex.rxjava3.core.Single

interface WeatherUseCase {
    fun getNewData()

    fun getCurrentPositionWeatherNow(lon: Double, lat: Double): Single<WeatherNowVO>

    fun getCurrentPositionWeather24Hour(lon: Double, lat: Double): Single<Weather24HourVO>

    fun getCurrentPositionWeatherWeekly(lon: Double, lat: Double): Single<WeatherWeeklyVO>

    fun getCurrentPositionSunriseSunset(lon: Double, lat: Double): Single<SunriseSunsetVO>

    fun getCurrentPositionWeatherOtherInfo(lon: Double, lat: Double): Single<WeatherOtherInfoVO>

    fun getCurrentPositionWeatherForecastText(lon: Double, lat: Double): Single<WeatherForecastTextVO>

    fun getCityPositionWeatherNow(city: String): Single<WeatherNowVO>

    fun getCityPositionWeather24Hour(city: String): Single<Weather24HourVO>

    fun getCityPositionWeatherWeekly(city: String): Single<WeatherWeeklyVO>

    fun getCityPositionWeatherOtherInfo(city: String): Single<WeatherOtherInfoVO>

    fun getCityPositionSunriseSunset(city: String): Single<SunriseSunsetVO>

    fun getCityPositionWeatherForecastText(city: String): Single<WeatherForecastTextVO>
}
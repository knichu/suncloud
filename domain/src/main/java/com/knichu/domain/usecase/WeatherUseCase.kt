package com.knichu.domain.useCase

import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyVO

interface WeatherUseCase {
    fun getNewData()

    suspend fun getCurrentPositionWeatherNow(lon: Double, lat: Double): WeatherNowVO

    suspend fun getCurrentPositionWeather24Hour(lon: Double, lat: Double): Weather24HourVO

    suspend fun getCurrentPositionWeatherWeekly(lon: Double, lat: Double): WeatherWeeklyVO

    suspend fun getCurrentPositionSunriseSunset(lon: Double, lat: Double): SunriseSunsetVO

    suspend fun getCurrentPositionWeatherOtherInfo(lon: Double, lat: Double): WeatherOtherInfoVO

    suspend fun getCurrentPositionWeatherForecastText(lon: Double, lat: Double): WeatherForecastTextVO

    suspend fun getCityPositionWeatherNow(city: String): WeatherNowVO

    suspend fun getCityPositionWeather24Hour(city: String): Weather24HourVO

    suspend fun getCityPositionWeatherWeekly(city: String): WeatherWeeklyVO

    suspend fun getCityPositionWeatherOtherInfo(city: String): WeatherOtherInfoVO

    suspend fun getCityPositionSunriseSunset(city: String): SunriseSunsetVO

    suspend fun getCityPositionWeatherForecastText(city: String): WeatherForecastTextVO

    suspend fun getStoredCityListWeatherNow(city: String): WeatherNowCityListItemVO
}

package com.knichu.domain.repository

import com.knichu.domain.param.OpenWeatherRequestParam
import com.knichu.domain.vo.OpenWeatherVO
interface OpenWeatherRepository {
    suspend fun getOpenWeather(param: OpenWeatherRequestParam): OpenWeatherVO
}
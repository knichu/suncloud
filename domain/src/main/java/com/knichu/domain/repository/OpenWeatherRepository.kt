package com.knichu.domain.repository

import com.knichu.domain.param.OpenWeatherRequestParam
import com.knichu.domain.vo.OpenWeatherVO
import io.reactivex.rxjava3.core.Single

interface OpenWeatherRepository {
    fun getOpenWeather(param: OpenWeatherRequestParam): Single<OpenWeatherVO>
}
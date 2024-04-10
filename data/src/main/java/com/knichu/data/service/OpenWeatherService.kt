package com.knichu.data.service

import com.knichu.data.dto.request.OpenWeatherRequestDTO
import com.knichu.data.dto.response.OpenWeatherResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface OpenWeatherService {
    @GET("weather")
    fun getOpenWeather(@Body openWeatherRequestDTO: OpenWeatherRequestDTO):
            Single<Response<OpenWeatherResponseDTO>>
}
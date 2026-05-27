package com.knichu.data.service

import com.knichu.data.dto.request.ApiKeys
import com.knichu.data.dto.response.OpenWeatherResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {
    @GET("weather")
    suspend fun getOpenWeather(
        @Query("lon") lon: Double? = null,
        @Query("lat") lat: Double? = null,
        @Query("appid") appid: String? = ApiKeys.OPEN_WEATHER_MAP_API_KEY,
        @Query("units") units: String? = "metric"
    ): Response<OpenWeatherResponseDTO>
}

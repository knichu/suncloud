package com.knichu.data.datasource

import com.knichu.data.dto.request.OpenWeatherRequestDTO
import com.knichu.data.dto.response.OpenWeatherResponseDTO
import com.knichu.data.service.OpenWeatherService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class OpenWeatherDataSource @Inject constructor(
    private val openWeatherService: OpenWeatherService
) : BaseNetworkDataSource() {
    fun getOpenWeather(openWeatherRequest: OpenWeatherRequestDTO): Single<OpenWeatherResponseDTO> {
        return openWeatherService.getOpenWeather(
            lon = openWeatherRequest.lon,
            lat = openWeatherRequest.lat
        )
            .subscribeOn(Schedulers.io())
            .map { checkResponse(it) }
    }
}
package com.knichu.data.service

import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.data.dto.response.LiveWeatherResponseDTO
import com.knichu.data.dto.response.LongRainCloudResponseDTO
import com.knichu.data.dto.response.LongTemperatureResponseDTO
import com.knichu.data.dto.response.ShortWeatherResponseDTO
import com.knichu.data.dto.response.WeatherForecastTextResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface WeatherService {
    @GET("VilageFcstInfoService_2.0/getUltraSrtNcst")
    fun getLiveWeather(@Body liveWeatherRequest: LiveWeatherRequestDTO):
            Single<Response<LiveWeatherResponseDTO>>

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    fun getShortWeather(@Body shortWeatherRequest: ShortWeatherRequestDTO):
            Single<Response<ShortWeatherResponseDTO>>

    @GET("MidFcstInfoService/getMidLandFcst")
    fun getLongRainCloud(@Body longRainCloudRequest: LongRainCloudRequestDTO):
            Single<Response<LongRainCloudResponseDTO>>

    @GET("MidFcstInfoService/getMidTa")
    fun getLongTemperature(@Body longTemperatureRequest: LongTemperatureRequestDTO):
            Single<Response<LongTemperatureResponseDTO>>

    @GET("MidFcstInfoService/getMidFcst")
    fun getWeatherForecastText(@Body weatherForecastTextRequest: WeatherForecastTextRequestDTO):
            Single<Response<WeatherForecastTextResponseDTO>>
}
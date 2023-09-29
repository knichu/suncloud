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
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface WeatherService {
    @GET("VilageFcstInfoService_2.0/getUltraSrtNcst")
    suspend fun getLiveWeather(@Body liveWeatherRequest: LiveWeatherRequestDTO):
            Response<LiveWeatherResponseDTO>

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    suspend fun getShortWeather(@Body shortWeatherRequest: ShortWeatherRequestDTO):
            Response<ShortWeatherResponseDTO>

    @GET("MidFcstInfoService/getMidLandFcst")
    suspend fun getLongRainCloud(@Body longRainCloudRequest: LongRainCloudRequestDTO):
            Response<LongRainCloudResponseDTO>

    @GET("MidFcstInfoService/getMidTa")
    suspend fun getLongTemperature(@Body longTemperatureRequest: LongTemperatureRequestDTO):
            Response<LongTemperatureResponseDTO>

    @GET("MidFcstInfoService/getMidFcst")
    suspend fun getWeatherForecastText(@Body weatherForecastTextRequest: WeatherForecastTextRequestDTO):
            Response<WeatherForecastTextResponseDTO>
}
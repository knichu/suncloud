package com.knichu.data.service

import com.knichu.data.dto.request.ApiKeys
import com.knichu.data.dto.response.LiveWeatherResponseDTO
import com.knichu.data.dto.response.LongRainCloudResponseDTO
import com.knichu.data.dto.response.LongTemperatureResponseDTO
import com.knichu.data.dto.response.MidWeatherResponseDTO
import com.knichu.data.dto.response.ShortWeatherResponseDTO
import com.knichu.data.dto.response.WeatherForecastTextResponseDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("VilageFcstInfoService_2.0/getUltraSrtNcst")
    suspend fun getLiveWeather(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 8,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("base_date") baseDate: String? = null,
        @Query("base_time") baseTime: String? = null,
        @Query("nx") nx: Long? = null,
        @Query("ny") ny: Long? = null
    ): Response<LiveWeatherResponseDTO>

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    suspend fun getShortWeather(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 290,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("base_date") baseDate: String? = null,
        @Query("base_time") baseTime: String? = null,
        @Query("nx") nx: Long? = null,
        @Query("ny") ny: Long? = null
    ): Response<ShortWeatherResponseDTO>

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    suspend fun getMidWeather(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1160,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("base_date") baseDate: String? = null,
        @Query("base_time") baseTime: String? = "2300",
        @Query("nx") nx: Long? = null,
        @Query("ny") ny: Long? = null
    ): Response<MidWeatherResponseDTO>

    @GET("MidFcstInfoService/getMidLandFcst")
    suspend fun getLongRainCloud(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("regId") regId: String? = null,
        @Query("tmFc") tmFc: Long? = null
    ): Response<LongRainCloudResponseDTO>

    @GET("MidFcstInfoService/getMidTa")
    suspend fun getLongTemperature(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("regId") regId: String? = null,
        @Query("tmFc") tmFc: Long? = null
    ): Response<LongTemperatureResponseDTO>

    @GET("MidFcstInfoService/getMidFcst")
    suspend fun getWeatherForecastText(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("stnId") stnId: String? = null,
        @Query("tmFc") tmFc: Long? = null
    ): Response<WeatherForecastTextResponseDTO>
}

package com.knichu.data.service

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.request.ApiKeys
import com.knichu.data.dto.request.LiveWeatherRequestDTO
import com.knichu.data.dto.request.LongRainCloudRequestDTO
import com.knichu.data.dto.request.LongTemperatureRequestDTO
import com.knichu.data.dto.request.MidWeatherRequestDTO
import com.knichu.data.dto.request.ShortWeatherRequestDTO
import com.knichu.data.dto.request.WeatherForecastTextRequestDTO
import com.knichu.data.dto.response.LiveWeatherResponseDTO
import com.knichu.data.dto.response.LongRainCloudResponseDTO
import com.knichu.data.dto.response.LongTemperatureResponseDTO
import com.knichu.data.dto.response.MidWeatherResponseDTO
import com.knichu.data.dto.response.ShortWeatherResponseDTO
import com.knichu.data.dto.response.WeatherForecastTextResponseDTO
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("VilageFcstInfoService_2.0/getUltraSrtNcst")
    fun getLiveWeather(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 8,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("base_date") baseDate: String? = null,
        @Query("base_time") baseTime: String? = null,
        @Query("nx") nx: Long? = null,
        @Query("ny") ny: Long? = null
    ): Single<Response<LiveWeatherResponseDTO>>

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    fun getShortWeather(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 290,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("base_date") baseDate: String? = null,
        @Query("base_time") baseTime: String? = null,
        @Query("nx") nx: Long? = null,
        @Query("ny") ny: Long? = null
    ): Single<Response<ShortWeatherResponseDTO>>

    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    fun getMidWeather(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 880,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("base_date") baseDate: String? = null,
        @Query("base_time") baseTime: String? = "2300",
        @Query("nx") nx: Long? = null,
        @Query("ny") ny: Long? = null
    ): Single<Response<MidWeatherResponseDTO>>

    @GET("MidFcstInfoService/getMidLandFcst")
    fun getLongRainCloud(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("regId") regId: String? = null,
        @Query("tmFc") tmFc: Long? = null
    ): Single<Response<LongRainCloudResponseDTO>>

    @GET("MidFcstInfoService/getMidTa")
    fun getLongTemperature(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("regId") regId: String? = null,
        @Query("tmFc") tmFc: Long? = null
    ): Single<Response<LongTemperatureResponseDTO>>

    @GET("MidFcstInfoService/getMidFcst")
    fun getWeatherForecastText(
        @Query("serviceKey") serviceKey: String? = ApiKeys.APIS_DATA_WEATHER_API_KEY,
        @Query("pageNo") pageNo: Long? = 1,
        @Query("numOfRows") numOfRows: Long? = 1,
        @Query("dataType") dataType: String? = ApiKeys.JSON,
        @Query("stnId") stnId: String? = null,
        @Query("tmFc") tmFc: Long? = null
    ): Single<Response<WeatherForecastTextResponseDTO>>
}
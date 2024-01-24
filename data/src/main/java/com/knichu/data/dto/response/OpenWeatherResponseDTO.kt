package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.OpenWeatherVO

data class OpenWeatherResponseDTO(
    @field:SerializedName("coord") val coord: Coord? = null,
    @field:SerializedName("weather") val weather: List<Weather>? = null,
    @field:SerializedName("base") val base: String? = null,
    @field:SerializedName("main") val main: Main? = null,
    @field:SerializedName("visibility") val visibility: Int? = null,
    @field:SerializedName("wind") val wind: Wind? = null,
    @field:SerializedName("clouds") val clouds: Clouds? = null,
    @field:SerializedName("dt") val dt: Int? = null,
    @field:SerializedName("sys") val sys: Sys? = null,
    @field:SerializedName("timezone") val timezone: Int? = null,
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("name") val name: String? = null,
    @field:SerializedName("cod") val cod: Int? = null
) {
    fun toDomain(): OpenWeatherVO {
        return OpenWeatherVO(
            weatherMain = weather?.firstOrNull()?.main,
            weatherDescription = weather?.firstOrNull()?.description,
            weatherIcon = weather?.firstOrNull()?.icon,
            temperature = main?.temp,
            feelsLike = main?.feelsLike,
            temperatureMin = main?.tempMin,
            temperatureMax = main?.tempMax,
            pressure = main?.pressure,
            humidity = main?.humidity,
            windSpeed = wind?.speed,
            windDirection = wind?.deg,
            sunrise = sys?.sunrise,
            sunset = sys?.sunset
        )
    }
}

data class Coord (
    @field:SerializedName("lon") val lon: Double? = null,
    @field:SerializedName("lat") val lat: Double? = null
)

data class Weather (
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("main") val main: String? = null,
    @field:SerializedName("description") val description: String? = null,
    @field:SerializedName("icon") val icon: String? = null
)

data class Main (
    @field:SerializedName("temp") val temp: Double? = null,
    @field:SerializedName("feels_like") val feelsLike: Double? = null,
    @field:SerializedName("temp_min") val tempMin: Double? = null,
    @field:SerializedName("temp_max") val tempMax: Double? = null,
    @field:SerializedName("pressure") val pressure: Int? = null,
    @field:SerializedName("humidity") val humidity: Int? = null,
    @field:SerializedName("sea_level") val seaLevel: Int? = null,
    @field:SerializedName("grnd_level") val grndLevel: Int? = null
)

data class Wind (
    @field:SerializedName("speed") val speed: Double? = null,
    @field:SerializedName("deg") val deg: Int? = null,
    @field:SerializedName("gust") val gust: Double? = null
)

data class Clouds (
    @field:SerializedName("all") val all: Int? = null
)

data class Sys (
    @field:SerializedName("type") val type: Int? = null,
    @field:SerializedName("id") val id: Int? = null,
    @field:SerializedName("country") val country: String? = null,
    @field:SerializedName("sunrise") val sunrise: Int? = null,
    @field:SerializedName("sunset") val sunset: Int? = null
)
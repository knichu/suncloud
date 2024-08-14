package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.CityLocationItemVO
import com.knichu.domain.vo.CityLocationVO

data class CityLocationResponseDTO(
    @field:SerializedName("cityList") val cityList: List<CityList>? = null,
) {
    fun toDomain(): CityLocationVO {
        return CityLocationVO(
            item = cityList?.map {
                CityLocationItemVO(
                    cityName = it.cityName,
                    longRainCloudParam = it.longRainCloudParam,
                    longTemperatureParam = it.longTemperatureParam,
                    weatherForecastTextParam = it.weatherForecastTextParam,
                    longitude = it.longitude,
                    latitude = it.latitude
                )
            }
        )
    }
}

data class CityList(
     @field:SerializedName("cityName") val cityName: String? = null,
     @field:SerializedName("longRainCloudParam") val longRainCloudParam: String? = null,
     @field:SerializedName("longTemperatureParam") val longTemperatureParam: String? = null,
     @field:SerializedName("weatherForecastTextParam") val weatherForecastTextParam: String? = null,
     @field:SerializedName("longitude") val longitude: String? = null,
     @field:SerializedName("latitude") val latitude: String? = null,
)


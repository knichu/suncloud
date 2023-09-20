package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.response.entity.weatherForecastText.WFTResponse

data class WeatherForecastTextResponseDTO(
    @field:SerializedName("response") val wFTResponse: WFTResponse? = null
)
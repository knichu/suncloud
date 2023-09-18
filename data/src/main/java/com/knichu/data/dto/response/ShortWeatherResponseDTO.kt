package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.response.entity.short.SWResponse

data class ShortWeatherResponseDTO(
    @field:SerializedName("response") val sWResponse: SWResponse? = null
)
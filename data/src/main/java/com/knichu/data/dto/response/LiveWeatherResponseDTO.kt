package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.response.entity.live.LWResponse

data class LiveWeatherResponseDTO(
    @field:SerializedName("response") val lWResponse: LWResponse? = null
)

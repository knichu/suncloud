package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.response.entity.longTemperature.LTResponse

data class LongTemperatureResponseDTO(
    @field:SerializedName("response") val lTResponse: LTResponse? = null
)
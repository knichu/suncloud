package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.response.entity.airPollution.APList
import com.knichu.data.dto.response.entity.airPollution.LonLat

data class AirPollutionResponseDTO(
    @field:SerializedName("coord") val coord: LonLat? = null,
    @field:SerializedName("list") val aPList: List<APList>? = null
)
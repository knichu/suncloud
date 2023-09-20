package com.knichu.data.dto.response.entity.weatherForecastText

import com.google.gson.annotations.SerializedName

data class WFTResponse(
    @field:SerializedName("header") val wFTHeader: WFTHeader? = null,
    @field:SerializedName("body") val wFTBody: WFTBody? = null
)
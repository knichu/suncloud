package com.knichu.data.dto.response.entity.weatherForecastText

import com.google.gson.annotations.SerializedName

data class WFTHeader(
    @field:SerializedName("resultCode") val resultCode: String,
    @field:SerializedName("resultMsg") val resultMsg: String
)
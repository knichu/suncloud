package com.knichu.data.dto.response.entity.shortWeather

import com.google.gson.annotations.SerializedName

data class SWHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

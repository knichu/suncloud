package com.knichu.data.dto.response.entity.liveWeather

import com.google.gson.annotations.SerializedName

data class LWHeader(
    @field:SerializedName("resultCode") val resultCode: String,
    @field:SerializedName("resultMsg") val resultMsg: String
)

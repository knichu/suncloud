package com.knichu.data.dto.response.entity.longTemperature

import com.google.gson.annotations.SerializedName

data class LTHeader(
    @field:SerializedName("resultCode") val resultCode: String,
    @field:SerializedName("resultMsg") val resultMsg: String
)
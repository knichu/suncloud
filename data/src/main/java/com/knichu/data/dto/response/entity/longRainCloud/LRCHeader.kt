package com.knichu.data.dto.response.entity.longRainCloud

import com.google.gson.annotations.SerializedName

data class LRCHeader(
    @field:SerializedName("resultCode") val resultCode: String,
    @field:SerializedName("resultMsg") val resultMsg: String
)
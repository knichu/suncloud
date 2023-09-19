package com.knichu.data.dto.response.entity.longRainCloud

import com.google.gson.annotations.SerializedName

data class LRCResponse(
    @field:SerializedName("header") val lRCHeader: LRCHeader,
    @field:SerializedName("body") val lRCBody: LRCBody? = null
)
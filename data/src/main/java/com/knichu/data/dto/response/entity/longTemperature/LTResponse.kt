package com.knichu.data.dto.response.entity.longTemperature

import com.google.gson.annotations.SerializedName

data class LTResponse(
    @field:SerializedName("header") val lTHeader: LTHeader,
    @field:SerializedName("body") val lTBody: LTBody? = null
)
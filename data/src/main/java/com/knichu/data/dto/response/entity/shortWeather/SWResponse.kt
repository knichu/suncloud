package com.knichu.data.dto.response.entity.shortWeather

import com.google.gson.annotations.SerializedName

data class SWResponse(
    @field:SerializedName("header") val sWHeader: SWHeader? = null,
    @field:SerializedName("body") val sWBody: SWBody? = null
)

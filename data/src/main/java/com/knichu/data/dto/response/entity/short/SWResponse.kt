package com.knichu.data.dto.response.entity.short

import com.google.gson.annotations.SerializedName

data class SWResponse(
    @field:SerializedName("header") val sWHeader: SWHeader,
    @field:SerializedName("body") val sWBody: SWBody? = null
)

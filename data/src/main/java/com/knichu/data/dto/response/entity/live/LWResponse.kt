package com.knichu.data.dto.response.entity.live

import com.google.gson.annotations.SerializedName

data class LWResponse(
    @field:SerializedName("header") val lWHeader: LWHeader,
    @field:SerializedName("body") val lWBody: LWBody? = null
)

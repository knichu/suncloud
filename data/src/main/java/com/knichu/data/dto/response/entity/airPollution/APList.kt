package com.knichu.data.dto.response.entity.airPollution

import com.google.gson.annotations.SerializedName

data class APList(
    @field:SerializedName("main") val main: Main? = null,
    @field:SerializedName("components") val components: Components? = null,
    @field:SerializedName("dt") val dt: Long? = null
)

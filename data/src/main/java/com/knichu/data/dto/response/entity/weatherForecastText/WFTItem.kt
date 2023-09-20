package com.knichu.data.dto.response.entity.weatherForecastText

import com.google.gson.annotations.SerializedName

data class WFTItem(
    @field:SerializedName("wfSv") val wfSv: String? = null
)

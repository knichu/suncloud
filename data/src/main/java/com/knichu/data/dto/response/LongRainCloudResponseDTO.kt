package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.data.dto.response.entity.longRainCloud.LRCResponse

data class LongRainCloudResponseDTO(
    @field:SerializedName("response") val lRCResponse: LRCResponse? = null
)
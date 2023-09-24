package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.LongRainCloudVO

data class LongRainCloudResponseDTO(
    @field:SerializedName("response") val response: LongRainCloudResponse? = null
) {
    fun toDomain(): LongRainCloudVO {
        return LongRainCloudVO(
            regionCode = requireNotNull(response?.body?.items?.firstOrNull()?.regId),
            rainProb3Am = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt3Am),
            rainProb3Pm = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt3Pm),
            rainProb4Am = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt4Am),
            rainProb4Pm = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt4Pm),
            rainProb5Am = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt5Am),
            rainProb5Pm = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt5Pm),
            rainProb6Am = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt6Am),
            rainProb6Pm = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt6Pm),
            rainProb7Am = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt7Am),
            rainProb7Pm = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt7Pm),
            rainProb8 = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt8),
            rainProb9 = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt9),
            rainProb10 = requireNotNull(response?.body?.items?.firstOrNull()?.rnSt10),
            weatherForecast3Am = requireNotNull(response?.body?.items?.firstOrNull()?.wf3Am),
            weatherForecast3Pm = requireNotNull(response?.body?.items?.firstOrNull()?.wf3Pm),
            weatherForecast4Am = requireNotNull(response?.body?.items?.firstOrNull()?.wf4Am),
            weatherForecast4Pm = requireNotNull(response?.body?.items?.firstOrNull()?.wf4Pm),
            weatherForecast5Am = requireNotNull(response?.body?.items?.firstOrNull()?.wf5Am),
            weatherForecast5Pm = requireNotNull(response?.body?.items?.firstOrNull()?.wf5Pm),
            weatherForecast6Am = requireNotNull(response?.body?.items?.firstOrNull()?.wf6Am),
            weatherForecast6Pm = requireNotNull(response?.body?.items?.firstOrNull()?.wf6Pm),
            weatherForecast7Am = requireNotNull(response?.body?.items?.firstOrNull()?.wf7Am),
            weatherForecast7Pm = requireNotNull(response?.body?.items?.firstOrNull()?.wf7Pm),
            weatherForecast8 = requireNotNull(response?.body?.items?.firstOrNull()?.wf8),
            weatherForecast9 = requireNotNull(response?.body?.items?.firstOrNull()?.wf9),
            weatherForecast10 = requireNotNull(response?.body?.items?.firstOrNull()?.wf10)
        )
    }
}

data class LongRainCloudResponse(
    @field:SerializedName("header") val header: LongRainCloudHeader? = null,
    @field:SerializedName("body") val body: LongRainCloudBody? = null
)

data class LongRainCloudHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

data class LongRainCloudBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val items: List<LongRainCloudItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class LongRainCloudItem(
    @field:SerializedName("regId") val regId: String? = null,
    @field:SerializedName("rnSt3Am") val rnSt3Am: Long? = null,
    @field:SerializedName("rnSt3Pm") val rnSt3Pm: Long? = null,
    @field:SerializedName("rnSt4Am") val rnSt4Am: Long? = null,
    @field:SerializedName("rnSt4Pm") val rnSt4Pm: Long? = null,
    @field:SerializedName("rnSt5Am") val rnSt5Am: Long? = null,
    @field:SerializedName("rnSt5Pm") val rnSt5Pm: Long? = null,
    @field:SerializedName("rnSt6Am") val rnSt6Am: Long? = null,
    @field:SerializedName("rnSt6Pm") val rnSt6Pm: Long? = null,
    @field:SerializedName("rnSt7Am") val rnSt7Am: Long? = null,
    @field:SerializedName("rnSt7Pm") val rnSt7Pm: Long? = null,
    @field:SerializedName("rnSt8") val rnSt8: Long? = null,
    @field:SerializedName("rnSt9") val rnSt9: Long? = null,
    @field:SerializedName("rnSt10") val rnSt10: Long? = null,
    @field:SerializedName("wf3Am") val wf3Am: String? = null,
    @field:SerializedName("wf3Pm") val wf3Pm: String? = null,
    @field:SerializedName("wf4Am") val wf4Am: String? = null,
    @field:SerializedName("wf4Pm") val wf4Pm: String? = null,
    @field:SerializedName("wf5Am") val wf5Am: String? = null,
    @field:SerializedName("wf5Pm") val wf5Pm: String? = null,
    @field:SerializedName("wf6Am") val wf6Am: String? = null,
    @field:SerializedName("wf6Pm") val wf6Pm: String? = null,
    @field:SerializedName("wf7Am") val wf7Am: String? = null,
    @field:SerializedName("wf7Pm") val wf7Pm: String? = null,
    @field:SerializedName("wf8") val wf8: String? = null,
    @field:SerializedName("wf9") val wf9: String? = null,
    @field:SerializedName("wf10") val wf10: String? = null
)
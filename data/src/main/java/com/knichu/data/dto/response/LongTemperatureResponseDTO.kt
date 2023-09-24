package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName
import com.knichu.domain.vo.LongTemperatureVO

data class LongTemperatureResponseDTO(
    @field:SerializedName("response") val response: LongTemperatureResponse? = null
) {
    fun toDomain(): LongTemperatureVO {
        return LongTemperatureVO(
            regionCode = requireNotNull(response?.body?.items?.firstOrNull()?.regId),
            temperatureMin3 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin3),
            temperatureMin3Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin3Low),
            temperatureMin3High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin3High),
            temperatureMax3 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax3),
            temperatureMax3Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax3Low),
            temperatureMax3High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax3High),
            temperatureMin4 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin4),
            temperatureMin4Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin4Low),
            temperatureMin4High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin4High),
            temperatureMax4 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax4),
            temperatureMax4Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax4Low),
            temperatureMax4High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax4High),
            temperatureMin5 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin5),
            temperatureMin5Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin5Low),
            temperatureMin5High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin5High),
            temperatureMax5 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax5),
            temperatureMax5Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax5Low),
            temperatureMax5High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax5High),
            temperatureMin6 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin6),
            temperatureMin6Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin6Low),
            temperatureMin6High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin6High),
            temperatureMax6 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax6),
            temperatureMax6Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax6Low),
            temperatureMax6High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax6High),
            temperatureMin7 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin7),
            temperatureMin7Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin7Low),
            temperatureMin7High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin7High),
            temperatureMax7 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax7),
            temperatureMax7Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax7Low),
            temperatureMax7High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax7High),
            temperatureMin8 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin8),
            temperatureMin8Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin8Low),
            temperatureMin8High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin8High),
            temperatureMax8 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax8),
            temperatureMax8Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax8Low),
            temperatureMax8High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax8High),
            temperatureMin9 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin9),
            temperatureMin9Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin9Low),
            temperatureMin9High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin9High),
            temperatureMax9 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax9),
            temperatureMax9Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax9Low),
            temperatureMax9High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax9High),
            temperatureMin10 = requireNotNull(response?.body?.items?.firstOrNull()?.taMin10),
            temperatureMin10Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMin10Low),
            temperatureMin10High = requireNotNull(response?.body?.items?.firstOrNull()?.taMin10High),
            temperatureMax10 = requireNotNull(response?.body?.items?.firstOrNull()?.taMax10),
            temperatureMax10Low = requireNotNull(response?.body?.items?.firstOrNull()?.taMax10Low),
            temperatureMax10High = requireNotNull(response?.body?.items?.firstOrNull()?.taMax10High)
        )
    }
}

data class LongTemperatureResponse(
    @field:SerializedName("header") val header: LongTemperatureHeader? = null,
    @field:SerializedName("body") val body: LongTemperatureBody? = null
)

data class LongTemperatureHeader(
    @field:SerializedName("resultCode") val resultCode: String? = null,
    @field:SerializedName("resultMsg") val resultMsg: String? = null
)

data class LongTemperatureBody(
    @field:SerializedName("dataType") val dataType: String? = null,
    @field:SerializedName("items") val items: List<LongTemperatureItem>? = null,
    @field:SerializedName("pageNo") val pageNo: Long? = null,
    @field:SerializedName("numOfRows") val numOfRows: Long? = null,
    @field:SerializedName("totalCount") val totalCount: Long? = null
)

data class LongTemperatureItem(
    @field:SerializedName("regId") val regId: String? = null,
    @field:SerializedName("taMin3") val taMin3: Long? = null,
    @field:SerializedName("taMin3Low") val taMin3Low: Long? = null,
    @field:SerializedName("taMin3High") val taMin3High: Long? = null,
    @field:SerializedName("taMax3") val taMax3: Long? = null,
    @field:SerializedName("taMax3Low") val taMax3Low: Long? = null,
    @field:SerializedName("taMax3High") val taMax3High: Long? = null,
    @field:SerializedName("taMin4") val taMin4: Long? = null,
    @field:SerializedName("taMin4Low") val taMin4Low: Long? = null,
    @field:SerializedName("taMin4High") val taMin4High: Long? = null,
    @field:SerializedName("taMax4") val taMax4: Long? = null,
    @field:SerializedName("taMax4Low") val taMax4Low: Long? = null,
    @field:SerializedName("taMax4High") val taMax4High: Long? = null,
    @field:SerializedName("taMin5") val taMin5: Long? = null,
    @field:SerializedName("taMin5Low") val taMin5Low: Long? = null,
    @field:SerializedName("taMin5High") val taMin5High: Long? = null,
    @field:SerializedName("taMax5") val taMax5: Long? = null,
    @field:SerializedName("taMax5Low") val taMax5Low: Long? = null,
    @field:SerializedName("taMax5High") val taMax5High: Long? = null,
    @field:SerializedName("taMin6") val taMin6: Long? = null,
    @field:SerializedName("taMin6Low") val taMin6Low: Long? = null,
    @field:SerializedName("taMin6High") val taMin6High: Long? = null,
    @field:SerializedName("taMax6") val taMax6: Long? = null,
    @field:SerializedName("taMax6Low") val taMax6Low: Long? = null,
    @field:SerializedName("taMax6High") val taMax6High: Long? = null,
    @field:SerializedName("taMin7") val taMin7: Long? = null,
    @field:SerializedName("taMin7Low") val taMin7Low: Long? = null,
    @field:SerializedName("taMin7High") val taMin7High: Long? = null,
    @field:SerializedName("taMax7") val taMax7: Long? = null,
    @field:SerializedName("taMax7Low") val taMax7Low: Long? = null,
    @field:SerializedName("taMax7High") val taMax7High: Long? = null,
    @field:SerializedName("taMin8") val taMin8: Long? = null,
    @field:SerializedName("taMin8Low") val taMin8Low: Long? = null,
    @field:SerializedName("taMin8High") val taMin8High: Long? = null,
    @field:SerializedName("taMax8") val taMax8: Long? = null,
    @field:SerializedName("taMax8Low") val taMax8Low: Long? = null,
    @field:SerializedName("taMax8High") val taMax8High: Long? = null,
    @field:SerializedName("taMin9") val taMin9: Long? = null,
    @field:SerializedName("taMin9Low") val taMin9Low: Long? = null,
    @field:SerializedName("taMin9High") val taMin9High: Long? = null,
    @field:SerializedName("taMax9") val taMax9: Long? = null,
    @field:SerializedName("taMax9Low") val taMax9Low: Long? = null,
    @field:SerializedName("taMax9High") val taMax9High: Long? = null,
    @field:SerializedName("taMin10") val taMin10: Long? = null,
    @field:SerializedName("taMin10Low") val taMin10Low: Long? = null,
    @field:SerializedName("taMin10High") val taMin10High: Long? = null,
    @field:SerializedName("taMax10") val taMax10: Long? = null,
    @field:SerializedName("taMax10Low") val taMax10Low: Long? = null,
    @field:SerializedName("taMax10High") val taMax10High: Long? = null
)
package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class LongTemperatureResponseDTO(
    @field:SerializedName("response") val lTResponse: LTResponse? = null
) {
    data class LTResponse(
        @field:SerializedName("header") val lTHeader: LTHeader,
        @field:SerializedName("body") val lTBody: LTBody? = null
    )

    data class LTHeader(
        @field:SerializedName("resultCode") val resultCode: String,
        @field:SerializedName("resultMsg") val resultMsg: String
    )

    data class LTBody(
        @field:SerializedName("dataType") val dataType: String? = null,
        @field:SerializedName("items") val lTItem: List<LTItem>? = null,
        @field:SerializedName("pageNo") val pageNo: Long? = null,
        @field:SerializedName("numOfRows") val numOfRows: Long? = null,
        @field:SerializedName("totalCount") val totalCount: Long? = null
    )

    data class LTItem(
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
}
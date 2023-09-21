package com.knichu.data.dto.response

import com.google.gson.annotations.SerializedName

data class LongRainCloudResponseDTO(
    @field:SerializedName("response") val lRCResponse: LRCResponse? = null
) {
    data class LRCResponse(
        @field:SerializedName("header") val lRCHeader: LRCHeader,
        @field:SerializedName("body") val lRCBody: LRCBody? = null
    )

    data class LRCHeader(
        @field:SerializedName("resultCode") val resultCode: String,
        @field:SerializedName("resultMsg") val resultMsg: String
    )

    data class LRCBody(
        @field:SerializedName("dataType") val dataType: String? = null,
        @field:SerializedName("items") val lRCItem: List<LRCItem>? = null,
        @field:SerializedName("pageNo") val pageNo: Long? = null,
        @field:SerializedName("numOfRows") val numOfRows: Long? = null,
        @field:SerializedName("totalCount") val totalCount: Long? = null
    )

    data class LRCItem(
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
}
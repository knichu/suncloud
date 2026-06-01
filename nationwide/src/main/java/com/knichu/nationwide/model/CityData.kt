package com.knichu.nationwide.model

data class CityInfo(
    val name: String,
    val lat: Double,
    val lon: Double,
    val tier: Int
)

// 지도에 표시할 도시와 티어 정의 — 좌표는 cityCode.json에서 런타임에 로드
val CITY_TIER_MAP: Map<String, Int> = mapOf(
    // Tier 1 — 광역시 및 특별시 (전국 뷰에서 표시)
    "서울" to 1, "부산" to 1, "대구" to 1, "인천" to 1,
    "광주" to 1, "대전" to 1, "울산" to 1, "세종" to 1, "제주" to 1,

    // Tier 2 — 주요 시 (확대 시 추가 표시)
    "수원" to 2, "고양" to 2, "창원" to 2, "청주" to 2,
    "전주" to 2, "천안" to 2, "춘천" to 2, "강릉" to 2,
    "포항" to 2, "여수" to 2, "목포" to 2, "안동" to 2,
    "구미" to 2, "경주" to 2, "속초" to 2, "서귀포" to 2,
)

const val ZOOM_TIER1_ONLY = 7.0

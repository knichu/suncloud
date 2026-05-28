package com.knichu.common_ui.enums

enum class WeatherIcon(val value: String, val icon: Int, val label: String) {
    SUNNY("1", com.knichu.common_ui.R.drawable.ic_sunny_rounded, "맑음"),
    CLOUDY_PARTLY("2", com.knichu.common_ui.R.drawable.ic_cloudy_partly, "구름 조금"),
    CLOUDY_PARTLY_RAINY("3", com.knichu.common_ui.R.drawable.ic_cloudy_partly_rainy, "구름 조금 후 비"),
    CLOUDY_PARTLY_SNOWING("4", com.knichu.common_ui.R.drawable.ic_cloudy_partly_snowing, "구름 조금 후 눈"),
    CLOUDY_PARTLY_WEATHER_MIX("5", com.knichu.common_ui.R.drawable.ic_cloudy_partly_weather_mix, "구름 조금 후 비/눈"),
    CLOUDY_PARTLY_RAINY_HEAVY("6", com.knichu.common_ui.R.drawable.ic_cloudy_partly_rainy_heavy, "구름 조금 후 폭우"),
    CLOUDY("7", com.knichu.common_ui.R.drawable.ic_cloudy, "흐림"),
    CLOUDY_RAINY("8", com.knichu.common_ui.R.drawable.ic_cloudy_rainy, "비"),
    CLOUDY_SNOWING("9", com.knichu.common_ui.R.drawable.ic_cloudy_snowing, "눈"),
    CLOUDY_WEATHER_MIX("10", com.knichu.common_ui.R.drawable.ic_cloudy_weather_mix, "비/눈"),
    CLOUDY_RAINY_HEAVY("11", com.knichu.common_ui.R.drawable.ic_cloudy_rainy_heavy, "폭우"),
    NIGHT_CLEAR("12", com.knichu.common_ui.R.drawable.ic_night_clear, "맑음"),
    NIGHT_CLOUDY_PARTLY("13", com.knichu.common_ui.R.drawable.ic_night_cloudy_partly, "구름 조금"),
    NIGHT_CLOUDY("14", com.knichu.common_ui.R.drawable.ic_night_cloudy, "흐림"),
    NONE("", com.knichu.common_ui.R.drawable.ic_none, "-");

    companion object {
        fun getIconImage(value: String?): WeatherIcon {
            return values().firstOrNull { it.value == value } ?: NONE
        }

        fun getLabel(value: String?): String {
            return values().firstOrNull { it.value == value }?.label ?: "-"
        }
    }
}

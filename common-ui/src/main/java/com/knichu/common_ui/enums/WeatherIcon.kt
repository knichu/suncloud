package com.knichu.common_ui.enums

enum class WeatherIcon(val value: String, val icon: Int) {
    SUNNY("1", com.knichu.common_ui.R.drawable.ic_sunny_rounded),
    CLOUDY_PARTLY("2", com.knichu.common_ui.R.drawable.ic_cloudy_partly),
    CLOUDY_PARTLY_RAINY("3", com.knichu.common_ui.R.drawable.ic_cloudy_partly_rainy),
    CLOUDY_PARTLY_SNOWING("4", com.knichu.common_ui.R.drawable.ic_cloudy_partly_snowing),
    CLOUDY_PARTLY_WEATHER_MIX("5", com.knichu.common_ui.R.drawable.ic_cloudy_partly_weather_mix),
    CLOUDY_PARTLY_RAINY_HEAVY("6", com.knichu.common_ui.R.drawable.ic_cloudy_partly_rainy_heavy),
    CLOUDY("7", com.knichu.common_ui.R.drawable.ic_cloudy),
    CLOUDY_RAINY("8", com.knichu.common_ui.R.drawable.ic_cloudy_rainy),
    CLOUDY_SNOWING("9", com.knichu.common_ui.R.drawable.ic_cloudy_snowing),
    CLOUDY_WEATHER_MIX("10", com.knichu.common_ui.R.drawable.ic_cloudy_weather_mix),
    CLOUDY_RAINY_HEAVY("11", com.knichu.common_ui.R.drawable.ic_cloudy_rainy_heavy),
    NIGHT_CLEAR("12", com.knichu.common_ui.R.drawable.ic_night_clear),
    NIGHT_CLOUDY_PARTLY("13", com.knichu.common_ui.R.drawable.ic_night_cloudy_partly),
    NIGHT_CLOUDY("14", com.knichu.common_ui.R.drawable.ic_night_cloudy),
    NONE("", com.knichu.common_ui.R.drawable.ic_none);

    companion object {
        fun getIconImage(value: String?): WeatherIcon {
            return values().firstOrNull { it.value == value } ?: NONE
        }
    }
}
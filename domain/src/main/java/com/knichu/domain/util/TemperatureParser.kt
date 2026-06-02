package com.knichu.domain.util

import com.knichu.domain.constants.WeatherTempUnit
import kotlin.math.roundToInt

object TemperatureParser {
    fun convert(celsius: String?, unit: WeatherTempUnit): String? {
        if (unit != WeatherTempUnit.FAHRENHEIT) return celsius
        val value = celsius?.toDoubleOrNull() ?: return celsius
        return ((value * 9.0 / 5.0) + 32.0).roundToInt().toString()
    }
}

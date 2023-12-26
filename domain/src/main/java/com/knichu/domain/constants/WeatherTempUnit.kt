package com.knichu.domain.constants

/**
 * Celsius - metric
 * Fahrenheit - imperial
 */
enum class WeatherTempUnit(val unit: String) {
	NONE("none"), CELSIUS("metric"), FAHRENHEIT("imperial");
}

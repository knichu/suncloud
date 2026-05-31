package com.knichu.domain.util

import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.MidWeatherVO
import com.knichu.domain.vo.WeatherWeeklyItemVO
import com.knichu.domain.vo.WeatherWeeklyVO
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

object WeatherWeeklyParser {

    fun getWeatherWeeklyVO(
        midWeather: MidWeatherVO,
        longRainCloud: LongRainCloudVO,
        longTemperature: LongTemperatureVO,
        baseDate: String,
        longTermForecastTime: String
    ): WeatherWeeklyVO {
        val midWeatherData = getMidWeatherData(midWeather, baseDate)
        val longWeatherData = getLongWeatherData(longRainCloud, longTemperature, longTermForecastTime)
        return WeatherWeeklyVO(midWeatherData + longWeatherData)
    }

    private fun getDayOfWeekList(): List<String> {
        val dayOfWeekList = mutableListOf<String>()
        val dayOfWeekListInt = mutableListOf<Int>()
        val calendar = Calendar.getInstance()
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        dayOfWeekListInt.add(dayOfWeek)
        while (dayOfWeekListInt.size < 8) {
            if ((dayOfWeekListInt.last() + 1) <= 7) {
                dayOfWeekListInt.add(dayOfWeekListInt.last() + 1)
            } else {
                dayOfWeekListInt.add(1)
            }
        }
        dayOfWeekList.add("오늘")
        for (index in 1..7) {
            when (dayOfWeekListInt[index]) {
                1 -> dayOfWeekList.add("일요일")
                2 -> dayOfWeekList.add("월요일")
                3 -> dayOfWeekList.add("화요일")
                4 -> dayOfWeekList.add("수요일")
                5 -> dayOfWeekList.add("목요일")
                6 -> dayOfWeekList.add("금요일")
                7 -> dayOfWeekList.add("토요일")
            }
        }
        return dayOfWeekList.toList()
    }

    private fun getMidWeatherData(
        midWeather: MidWeatherVO,
        baseDate: String
    ): List<WeatherWeeklyItemVO> {
        val weatherWeeklyItemList = mutableListOf<WeatherWeeklyItemVO>()
        val dayOfWeekList = getDayOfWeekList()

        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val tempDate = Calendar.getInstance()
        tempDate.time = dateFormat.parse(baseDate)!!
        var checkIndex = 0
        for (index in 0..3) {
            var tempRainProbability: Int? = null
            val weatherConditionAMListSKY: MutableList<Int> = mutableListOf()
            val weatherConditionAMListPTY: MutableList<Int> = mutableListOf()
            var weatherConditionAM: Int?
            val weatherConditionPMListSKY: MutableList<Int> = mutableListOf()
            val weatherConditionPMListPTY: MutableList<Int> = mutableListOf()
            var weatherConditionPM: Int?
            var highestTemperature: String? = null
            var lowestTemperature: String? = null

            tempDate.add(Calendar.DAY_OF_MONTH, 1)
            val checkDate = dateFormat.format(tempDate.time)

            while (midWeather.item?.get(checkIndex)?.forecastDate == checkDate) {
                if (midWeather.item?.get(checkIndex)?.category == "POP") {
                    tempRainProbability = max(
                        tempRainProbability ?: 0,
                        midWeather.item[checkIndex].forecastValue?.toInt() ?: 0
                    )
                }
                if (midWeather.item?.get(checkIndex)?.category == "SKY") {
                    if ((midWeather.item[checkIndex].forecastTime?.toInt() ?: 0) < 1200) {
                        weatherConditionAMListSKY.add(midWeather.item[checkIndex].forecastValue?.toInt() ?: 1)
                        weatherConditionAMListPTY.add(midWeather.item[checkIndex + 1].forecastValue?.toInt() ?: 0)
                    } else {
                        weatherConditionPMListSKY.add(midWeather.item[checkIndex].forecastValue?.toInt() ?: 1)
                        weatherConditionPMListPTY.add(midWeather.item[checkIndex + 1].forecastValue?.toInt() ?: 0)
                    }
                }
                if (midWeather.item?.get(checkIndex)?.forecastTime == "1500") {
                    if (midWeather.item[checkIndex].category == "TMX") {
                        highestTemperature = midWeather.item[checkIndex].forecastValue?.toDouble()
                            ?.roundToInt()
                            .toString()
                    }
                }
                if (midWeather.item?.get(checkIndex)?.forecastTime == "0600") {
                    if (midWeather.item[checkIndex].category == "TMN") {
                        lowestTemperature = midWeather.item[checkIndex].forecastValue?.toDouble()
                            ?.roundToInt()
                            .toString()
                    }
                }
                checkIndex += 1
            }

            if (weatherConditionAMListPTY.maxOfOrNull { it } == 0) {
                weatherConditionAM = when {
                    weatherConditionAMListSKY.average() <= 1.5 -> 1
                    weatherConditionAMListSKY.average() <= 3.6 -> 2
                    else -> 7
                }
            } else {
                val mostCommonSKY = weatherConditionAMListSKY.groupingBy { it }.eachCount()
                    .filter { it.key == 3 || it.key == 4 }.toList()
                    .maxWithOrNull(compareBy({ it.second }, { it.first }))?.first ?: 3
                val mostCommonPTY = weatherConditionAMListPTY.groupingBy { it }.eachCount()
                    .filter { it.value == weatherConditionAMListPTY.groupingBy { it }.eachCount().values.maxOrNull() }
                    .keys.toList().maxOrNull()
                weatherConditionAM = when (mostCommonPTY) {
                    1 -> if (mostCommonSKY == 4) 8 else 3
                    2 -> if (mostCommonSKY == 4) 10 else 5
                    3 -> if (mostCommonSKY == 4) 9 else 4
                    4 -> if (mostCommonSKY == 4) 11 else 6
                    else -> 3
                }
            }

            if (weatherConditionPMListPTY.maxOfOrNull { it } == 0) {
                weatherConditionPM = when {
                    weatherConditionPMListSKY.average() <= 1.5 -> 1
                    weatherConditionPMListSKY.average() <= 3.6 -> 2
                    else -> 7
                }
            } else {
                val mostCommonSKY = weatherConditionAMListSKY.groupingBy { it }.eachCount()
                    .filter { it.key == 3 || it.key == 4 }.toList()
                    .maxWithOrNull(compareBy({ it.second }, { it.first }))?.first ?: 3
                val mostCommonPTY = weatherConditionPMListPTY.groupingBy { it }.eachCount()
                    .filter { it.value == weatherConditionPMListPTY.groupingBy { it }.eachCount().values.maxOrNull() }
                    .keys.toList().maxOrNull()
                weatherConditionPM = when (mostCommonPTY) {
                    1 -> if (mostCommonSKY == 4) 8 else 3
                    2 -> if (mostCommonSKY == 4) 10 else 5
                    3 -> if (mostCommonSKY == 4) 9 else 4
                    4 -> if (mostCommonSKY == 4) 11 else 6
                    else -> 3
                }
            }

            weatherWeeklyItemList.add(
                WeatherWeeklyItemVO(
                    dayOfTheWeek = dayOfWeekList[index],
                    rainProbability = tempRainProbability.toString(),
                    weatherConditionAM = weatherConditionAM.toString(),
                    weatherConditionPM = weatherConditionPM.toString(),
                    maxTemperature = "$highestTemperature",
                    minTemperature = "$lowestTemperature"
                )
            )
        }

        return weatherWeeklyItemList
    }

    private fun getLongWeatherData(
        longRainCloud: LongRainCloudVO,
        longTemperature: LongTemperatureVO,
        longTermForecastTime: String
    ): List<WeatherWeeklyItemVO> {
        val weatherWeeklyItemList = mutableListOf<WeatherWeeklyItemVO>()
        val dayOfWeekList = getDayOfWeekList()

        // 발표 기준일과 오늘의 차이를 계산해서 D+N 인덱스 보정
        // 오전 6시 이전에는 전날 0600 발표 기준을 사용하므로 D+N이 1일 앞당겨짐
        val forecastDateStr = longTermForecastTime.substring(0, 8) // "yyyyMMdd"
        val todayDateStr = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(Calendar.getInstance().time)
        val forecastCal = Calendar.getInstance().also {
            it.time = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(forecastDateStr)!!
        }
        val todayCal = Calendar.getInstance().also {
            it.time = SimpleDateFormat("yyyyMMdd", Locale.getDefault()).parse(todayDateStr)!!
        }
        val dayOffset = ((todayCal.timeInMillis - forecastCal.timeInMillis) / (1000 * 60 * 60 * 24)).toInt()

        for (index in 4..7) {
            val apiIndex = index + dayOffset
            val rainProbAm = when (apiIndex) {
                4 -> longRainCloud.rainProb4Am
                5 -> longRainCloud.rainProb5Am
                6 -> longRainCloud.rainProb6Am
                7 -> longRainCloud.rainProb7Am
                8 -> longRainCloud.rainProb8
                else -> 0
            } ?: 0
            val rainProbPm = when (apiIndex) {
                4 -> longRainCloud.rainProb4Pm
                5 -> longRainCloud.rainProb5Pm
                6 -> longRainCloud.rainProb6Pm
                7 -> longRainCloud.rainProb7Pm
                8 -> longRainCloud.rainProb8
                else -> 0
            } ?: 0

            val weatherConditionAMString = when (apiIndex) {
                4 -> longRainCloud.weatherForecast4Am
                5 -> longRainCloud.weatherForecast5Am
                6 -> longRainCloud.weatherForecast6Am
                7 -> longRainCloud.weatherForecast7Am
                8 -> longRainCloud.weatherForecast8
                else -> null
            } ?: "맑음"
            val weatherConditionAM = mapWeatherString(weatherConditionAMString)

            val weatherConditionPMString = when (apiIndex) {
                4 -> longRainCloud.weatherForecast4Pm
                5 -> longRainCloud.weatherForecast5Pm
                6 -> longRainCloud.weatherForecast6Pm
                7 -> longRainCloud.weatherForecast7Pm
                8 -> longRainCloud.weatherForecast8
                else -> null
            } ?: "맑음"
            val weatherConditionPM = mapWeatherString(weatherConditionPMString)

            val maxTemperature = when (apiIndex) {
                4 -> longTemperature.temperatureMax4
                5 -> longTemperature.temperatureMax5
                6 -> longTemperature.temperatureMax6
                7 -> longTemperature.temperatureMax7
                8 -> longTemperature.temperatureMax8
                else -> 0
            } ?: 0

            val minTemperature = when (apiIndex) {
                4 -> longTemperature.temperatureMin4
                5 -> longTemperature.temperatureMin5
                6 -> longTemperature.temperatureMin6
                7 -> longTemperature.temperatureMin7
                8 -> longTemperature.temperatureMin8
                else -> 0
            } ?: 0

            weatherWeeklyItemList.add(
                WeatherWeeklyItemVO(
                    dayOfTheWeek = dayOfWeekList[index],
                    rainProbability = max(rainProbAm, rainProbPm).toString(),
                    weatherConditionAM = weatherConditionAM.toString(),
                    weatherConditionPM = weatherConditionPM.toString(),
                    maxTemperature = "$maxTemperature",
                    minTemperature = "$minTemperature"
                )
            )
        }

        return weatherWeeklyItemList
    }

    private fun mapWeatherString(weather: String): Int = when (weather) {
        "맑음" -> 1
        "구름많음" -> 2
        "구름많고 비" -> 3
        "구름많고 눈" -> 4
        "구름많고 비/눈" -> 5
        "구름많고 소나기" -> 6
        "흐림" -> 7
        "흐리고 비" -> 8
        "흐리고 눈" -> 9
        "흐리고 비/눈" -> 10
        "흐리고 소나기" -> 11
        else -> 1
    }
}

package com.knichu.domain.util

import android.util.Log
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.MidWeatherVO
import com.knichu.domain.vo.ShortWeatherItemVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherWeeklyItemVO
import com.knichu.domain.vo.WeatherWeeklyVO
import io.reactivex.rxjava3.core.Single
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Locale
import kotlin.math.max
import kotlin.math.roundToInt

object WeatherWeeklyParser {

    fun getWeatherWeeklyVO(
        midWeatherVO: Single<MidWeatherVO>,
        longRainCloudVO: Single<LongRainCloudVO>,
        longTemperatureVO: Single<LongTemperatureVO>,
        baseDate: String
    ): Single<WeatherWeeklyVO> {
        return Single.zip(
            midWeatherVO,
            longRainCloudVO,
            longTemperatureVO
        ) { midWeather, longRainCloud, longTemperature ->
            val midWeatherData = getMidWeatherData(midWeather, baseDate)
            val longWeatherData = getLongWeatherData(longRainCloud, longTemperature)
            WeatherWeeklyVO(midWeatherData + longWeatherData)
        }
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
        Log.d("파서", "getWeatherWeeklyVO.getMidWeatherData 시작")
        val weatherWeeklyItemList = mutableListOf<WeatherWeeklyItemVO>()
        val dayOfWeekList = getDayOfWeekList()
        Log.d("파서", "getWeatherWeeklyVO.getMidWeatherData 시작1")

        // 오늘 ~ 2일후
        val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val tempDate = Calendar.getInstance()
        tempDate.time = dateFormat.parse(baseDate)!!
        var checkIndex = 0
        for (index in 0..2) {
            var tempRainProbability: Int? = null
            val weatherConditionAMListSKY: MutableList<Int> = mutableListOf()
            val weatherConditionAMListPTY: MutableList<Int> = mutableListOf()
            var weatherConditionAM: Int?
            val weatherConditionPMListSKY: MutableList<Int> = mutableListOf()
            val weatherConditionPMListPTY: MutableList<Int> = mutableListOf()
            var weatherConditionPM: Int?
            var highestTemperature: String? = null
            var lowestTemperature: String? = null

            // 확인할 날짜 설정
            tempDate.add(Calendar.DAY_OF_MONTH, 1)
            val checkDate = dateFormat.format(tempDate.time)

            // shortWeatherVO 체크
            while (midWeather.item?.get(checkIndex)?.forecastDate == checkDate) {

                // 강수확률
                if (midWeather.item?.get(checkIndex)?.category == "POP") {
                    tempRainProbability = max(
                        tempRainProbability ?: 0,
                        midWeather.item[checkIndex].forecastValue?.toInt() ?: 0
                    )
                }

                // 날씨상황(오전, 오후) 리스트에 저장 -> while 탈출 후 계산
                if (midWeather.item?.get(checkIndex)?.category == "SKY") {
                    if ((midWeather.item[checkIndex].forecastTime?.toInt() ?: 0) < 1200) {
                        weatherConditionAMListSKY.add(midWeather.item[checkIndex].forecastValue?.toInt() ?: 1)
                        weatherConditionAMListPTY.add(midWeather.item[checkIndex + 1].forecastValue?.toInt() ?: 0)
                    } else {
                        weatherConditionPMListSKY.add(midWeather.item[checkIndex].forecastValue?.toInt() ?: 1)
                        weatherConditionPMListPTY.add(midWeather.item[checkIndex + 1].forecastValue?.toInt() ?: 0)
                    }
                }

                // 최고, 최저기온
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

                // 다음 item 탐색
                checkIndex += 1
            }

            // 날씨상황(오전, 오후) 계산
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
                    1 -> when (mostCommonSKY) {
                        3 -> 3
                        4 -> 8
                        else -> 3
                    }
                    2 -> when (mostCommonSKY) {
                        3 -> 5
                        4 -> 10
                        else -> 5
                    }
                    3 -> when (mostCommonSKY) {
                        3 -> 4
                        4 -> 9
                        else -> 4
                    }
                    4 -> when (mostCommonSKY) {
                        3 -> 6
                        4 -> 11
                        else -> 6
                    }
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
                    1 -> when (mostCommonSKY) {
                        3 -> 3
                        4 -> 8
                        else -> 3
                    }
                    2 -> when (mostCommonSKY) {
                        3 -> 5
                        4 -> 10
                        else -> 5
                    }
                    3 -> when (mostCommonSKY) {
                        3 -> 4
                        4 -> 9
                        else -> 4
                    }
                    4 -> when (mostCommonSKY) {
                        3 -> 6
                        4 -> 11
                        else -> 6
                    }
                    else -> 3
                }
            }


            val tempWeatherWeeklyItem = WeatherWeeklyItemVO(
                dayOfTheWeek = dayOfWeekList[index],
                rainProbability = tempRainProbability.toString(),
                weatherConditionAM = weatherConditionAM.toString(),
                weatherConditionPM = weatherConditionPM.toString(),
                maxTemperature = "$highestTemperature",
                minTemperature = "$lowestTemperature"
            )

            weatherWeeklyItemList.add(tempWeatherWeeklyItem)
        }

        return weatherWeeklyItemList
    }

    private fun getLongWeatherData(
        longRainCloud: LongRainCloudVO,
        longTemperature: LongTemperatureVO
    ): List<WeatherWeeklyItemVO> {

        val weatherWeeklyItemList = mutableListOf<WeatherWeeklyItemVO>()
        val dayOfWeekList = getDayOfWeekList()

        // 3일후 ~ 7일후
        for (index in 3..7) {
            // 강수확률
            val rainProbAm = when (index) {
                3 -> longRainCloud.rainProb3Am
                4 -> longRainCloud.rainProb4Am
                5 -> longRainCloud.rainProb5Am
                6 -> longRainCloud.rainProb6Am
                7 -> longRainCloud.rainProb7Am
                else -> 0
            } ?: 0
            val rainProbPm = when (index) {
                3 -> longRainCloud.rainProb3Pm
                4 -> longRainCloud.rainProb4Pm
                5 -> longRainCloud.rainProb5Pm
                6 -> longRainCloud.rainProb6Pm
                7 -> longRainCloud.rainProb7Pm
                else -> 0
            } ?: 0

            // 오전날씨
            val weatherConditionAMString = when (index) {
                3 -> longRainCloud.weatherForecast3Am
                4 -> longRainCloud.weatherForecast4Am
                5 -> longRainCloud.weatherForecast5Am
                6 -> longRainCloud.weatherForecast6Am
                7 -> longRainCloud.weatherForecast7Am
                else -> 0
            } ?: "맑음"
            val weatherConditionAM = when (weatherConditionAMString) {
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

            // 오후날씨
            val weatherConditionPMString = when (index) {
                3 -> longRainCloud.weatherForecast3Pm
                4 -> longRainCloud.weatherForecast4Pm
                5 -> longRainCloud.weatherForecast5Pm
                6 -> longRainCloud.weatherForecast6Pm
                7 -> longRainCloud.weatherForecast7Pm
                else -> 0
            } ?: "맑음"
            val weatherConditionPM = when (weatherConditionPMString) {
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

            // 최고기온
            val maxTemperature = when (index) {
                3 -> longTemperature.temperatureMax3
                4 -> longTemperature.temperatureMax4
                5 -> longTemperature.temperatureMax5
                6 -> longTemperature.temperatureMax6
                7 -> longTemperature.temperatureMax7
                else -> 0
            } ?: 0

            // 최저기온
            val minTemperature = when (index) {
                3 -> longTemperature.temperatureMin3
                4 -> longTemperature.temperatureMin4
                5 -> longTemperature.temperatureMin5
                6 -> longTemperature.temperatureMin6
                7 -> longTemperature.temperatureMin7
                else -> 0
            } ?: 0

            val tempWeatherWeeklyItem = WeatherWeeklyItemVO(
                dayOfTheWeek = dayOfWeekList[index],
                rainProbability = max(rainProbAm, rainProbPm).toString(),
                weatherConditionAM = weatherConditionAM.toString(),
                weatherConditionPM = weatherConditionPM.toString(),
                maxTemperature = "$maxTemperature",
                minTemperature = "$minTemperature"
            )

            weatherWeeklyItemList.add(tempWeatherWeeklyItem)
        }

        return weatherWeeklyItemList
    }
}
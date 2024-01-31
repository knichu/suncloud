package com.knichu.domain.usecase

import com.knichu.domain.BuildConfig
import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.OpenWeatherRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.repository.OpenWeatherRepository
import com.knichu.domain.repository.WeatherRepository
import com.knichu.domain.util.CurrentPositionCityParser
import com.knichu.domain.util.DateTimeParser
import com.knichu.domain.util.LocationParser
import com.knichu.domain.util.SunriseSunsetParser
import com.knichu.domain.util.Weather24HourParser
import com.knichu.domain.util.WeatherNowParser
import com.knichu.domain.util.WeatherOtherInfoParser
import com.knichu.domain.util.WeatherWeeklyParser
import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val openWeatherRepository: OpenWeatherRepository,
    private val cityLocationRepository: CityLocationRepository
) {
    private val JSON = "JSON"
    private val allCityList = cityLocationRepository.getAllCityLocation()
    private val openWeatherMapApiKey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
    private val apisDataWeatherApiKey = BuildConfig.APIS_DATA_WEATHER_API_KEY

    private fun getCurrentPositionCity(lon: Double, lat: Double): Single<String> {
        return allCityList
            .map { cityLocationVO ->
                val nearestCityLocationItemVO = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                nearestCityLocationItemVO?.cityName.toString()
            }
    }

    fun getCurrentPositionWeatherNow(lon: Double, lat: Double): Single<WeatherNowVO> {
        val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
        val currentDate = adjustedTime.substring(0, 8).toLong()
        val currentTime = adjustedTime.substring(8).toLong()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val liveWeatherVO = weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                serviceKey = apisDataWeatherApiKey,
                pageNo = 1,
                numOfRows = 8,
                dataType = JSON,
                baseDate = currentDate,
                baseTime = currentTime, // 변경 가능
                nx = adjustX,
                ny = adjustY
            )
        )
        val currentPositionCity = getCurrentPositionCity(lon, lat)
        return WeatherNowParser.getWeatherNowVO(liveWeatherVO, currentPositionCity)
    }

    fun getCurrentPositionWeather24Hour(lon: Double, lat: Double): Single<Weather24HourVO> {
        val adjustedTime = DateTimeParser.adjustShortWeatherTime()
        val currentDate = adjustedTime.substring(0, 8).toLong()
        val currentTime = adjustedTime.substring(8).toLong()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val shortWeatherVO = weatherRepository.getShortWeather(
            ShortWeatherRequestParam(
                serviceKey = apisDataWeatherApiKey,
                pageNo = 1,
                numOfRows = 290,
                dataType = JSON,
                baseDate = currentDate,
                baseTime = currentTime,
                nx = adjustX,
                ny = adjustY
            )
        )
        return Weather24HourParser.getWeather24HourVO(shortWeatherVO)
    }

    fun getCurrentPositionWeatherWeekly(lon: Double, lat: Double): Single<WeatherWeeklyVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedShortTime = DateTimeParser.adjustShortWeatherTimeWeekly()
                val adjustedShortDate = adjustedShortTime.toLong()
                val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
                val shortWeatherVO = weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 880,
                        dataType = JSON,
                        baseDate = adjustedShortDate,
                        baseTime = 2300,
                        nx = adjustX,
                        ny = adjustY
                    )
                )

                val nearestCityLocationItemVO = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedLongDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentLongDateTime = adjustedLongDateTime.toLong()
                val longRainCloudVO = weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = nearestCityLocationItemVO?.longRainCloudParam,
                        tmFc = currentLongDateTime
                    )
                )
                val longTemperatureVO = weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = nearestCityLocationItemVO?.longTemperatureParam,
                        tmFc = currentLongDateTime
                    )
                )

                WeatherWeeklyParser.getWeatherWeeklyVO(
                    shortWeatherVO,
                    longRainCloudVO,
                    longTemperatureVO,
                    adjustedShortDate.toString()
                )
            }
    }

    fun getCurrentPositionSunriseSunset(lon: Double, lat: Double): Single<SunriseSunsetVO> {
        val openWeatherVO = openWeatherRepository.getOpenWeather(
            OpenWeatherRequestParam(
                lon = lon,
                lat = lat,
                appid = openWeatherMapApiKey,
                units = "metric"
            )
        )
        return SunriseSunsetParser.getSunriseSunsetVO(openWeatherVO)
    }

    fun getCurrentPositionWeatherOtherInfo(lon: Double, lat: Double): Single<WeatherOtherInfoVO> {
        val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
        val currentDate = adjustedTime.substring(0, 8).toLong()
        val currentTime = adjustedTime.substring(8).toLong()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val liveWeatherVO = weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                serviceKey = apisDataWeatherApiKey,
                pageNo = 1,
                numOfRows = 8,
                dataType = JSON,
                baseDate = currentDate,
                baseTime = currentTime, // 변경 가능
                nx = adjustX,
                ny = adjustY
            )
        )
        return WeatherOtherInfoParser.getWeatherOtherInfoVO(liveWeatherVO)
    }

    fun getCurrentPositionWeatherForecastText(lon: Double, lat: Double): Single<WeatherForecastTextVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val nearestCityLocationItemVO = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()

                weatherRepository.getWeatherForecastText(
                    WeatherForecastTextRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        stnId = nearestCityLocationItemVO?.weatherForecastTextParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    fun getCityPositionWeatherNow(city: String): Single<WeatherNowVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
                val currentDate = adjustedTime.substring(0, 8).toLong()
                val currentTime = adjustedTime.substring(8).toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val liveWeatherVO = weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 8,
                        dataType = JSON,
                        baseDate = currentDate,
                        baseTime = currentTime,
                        nx = cityLocationItemVO?.longitude?.toLong(),
                        ny = cityLocationItemVO?.latitude?.toLong()
                    )
                )
                WeatherNowParser.getWeatherNowVO(liveWeatherVO, cityLocationItemVO?.cityName)
            }
    }

    fun getCityPositionWeather24Hour(city: String): Single<Weather24HourVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = DateTimeParser.adjustShortWeatherTime()
                val currentDate = adjustedTime.substring(0, 8).toLong()
                val currentTime = adjustedTime.substring(8).toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val shortWeatherVO = weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 290,
                        dataType = JSON,
                        baseDate = currentDate,
                        baseTime = currentTime,
                        nx = cityLocationItemVO?.longitude?.toLong(),
                        ny = cityLocationItemVO?.latitude?.toLong()
                    )
                )
                Weather24HourParser.getWeather24HourVO(shortWeatherVO)
            }
    }

    fun getCityPositionWeatherWeekly(city: String): Single<WeatherWeeklyVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                val adjustedShortTime = DateTimeParser.adjustShortWeatherTimeWeekly()
                val adjustedShortDate = adjustedShortTime.toLong()
                val shortWeatherVO = weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 880,
                        dataType = JSON,
                        baseDate = adjustedShortDate,
                        baseTime = 2300,
                        nx = cityLocationItemVO?.longitude?.toLong(),
                        ny = cityLocationItemVO?.latitude?.toLong()
                    )
                )

                val adjustedLongDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentLongDateTime = adjustedLongDateTime.toLong()
                val longRainCloudVO = weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = cityLocationItemVO?.longRainCloudParam,
                        tmFc = currentLongDateTime
                    )
                )
                val longTemperatureVO = weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = cityLocationItemVO?.longTemperatureParam,
                        tmFc = currentLongDateTime
                    )
                )

                WeatherWeeklyParser.getWeatherWeeklyVO(
                    shortWeatherVO,
                    longRainCloudVO,
                    longTemperatureVO,
                    adjustedShortDate.toString()
                )
            }
    }

    fun getCityPositionWeatherOtherInfo(city: String): Single<WeatherOtherInfoVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
                val currentDate = adjustedTime.substring(0, 8).toLong()
                val currentTime = adjustedTime.substring(8).toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val liveWeatherVO = weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 8,
                        dataType = JSON,
                        baseDate = currentDate,
                        baseTime = currentTime, // 변경 가능
                        nx = cityLocationItemVO?.longitude?.toLong(),
                        ny = cityLocationItemVO?.latitude?.toLong()
                    )
                )
                WeatherOtherInfoParser.getWeatherOtherInfoVO(liveWeatherVO)
            }
    }

    fun getCityPositionSunriseSunset(city: String): Single<SunriseSunsetVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val openWeatherVO = openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble(),
                        appid = openWeatherMapApiKey,
                        units = "metric"
                    )
                )
                SunriseSunsetParser.getSunriseSunsetVO(openWeatherVO)
            }
    }

    fun getCityPositionWeatherForecastText(city: String): Single<WeatherForecastTextVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getWeatherForecastText(
                    WeatherForecastTextRequestParam(
                        serviceKey = apisDataWeatherApiKey,
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        stnId = cityLocationItemVO?.weatherForecastTextParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }
}
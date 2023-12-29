package com.knichu.domain.usecase

import com.knichu.domain.datastore.WeatherDataStore
import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.repository.WeatherRepository
import com.knichu.domain.util.CurrentPositionCityParser
import com.knichu.domain.util.DateTimeParser
import com.knichu.domain.util.LocationParser
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.LongRainCloudVO
import com.knichu.domain.vo.LongTemperatureVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.WeatherForecastTextVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val cityLocationRepository: CityLocationRepository,
    private val weatherDataStore: WeatherDataStore
) {
    private val timeParser = DateTimeParser()
    private val locationParser = LocationParser()
    private val currentPositionCityParser = CurrentPositionCityParser()
    private val JSON = "JSON"
    private val allCityList = cityLocationRepository.getAllCityLocation()

    fun getCurrentPositionCity(lon: Double, lat: Double): Single<String> {
        return allCityList
            .map { cityLocationVO ->
                val nearestCityLocationItemVO = currentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                nearestCityLocationItemVO?.cityName.toString()
            }
    }

    fun getCurrentPositionLiveWeather(lon: Double, lat: Double): Single<LiveWeatherVO> {
        val adjustedTime = timeParser.adjustLiveWeatherTime()
        val currentDate = adjustedTime.substring(0, 8).toLong()
        val currentTime = adjustedTime.substring(8).toLong()
        val (adjustX, adjustY) = locationParser.getNxNy(lon, lat)
        return weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                serviceKey = "my_service_key",
                pageNo = 1,
                numOfRows = 8,
                dataType = JSON,
                baseDate = currentDate,
                baseTime = currentTime, // 변경 가능
                nx = adjustX,
                ny = adjustY
            )
        )
    }

    fun getCurrentPositionShortWeather(lon: Double, lat: Double): Single<ShortWeatherVO> {
        val adjustedTime = timeParser.adjustShortWeatherTime()
        val currentDate = adjustedTime.substring(0, 8).toLong()
        val currentTime = adjustedTime.substring(9).toLong()
        val (adjustX, adjustY) = locationParser.getNxNy(lon, lat)
        return weatherRepository.getShortWeather(
            ShortWeatherRequestParam(
                serviceKey = "my_service_key",
                pageNo = 1,
                numOfRows = 290,
                dataType = JSON,
                baseDate = currentDate,
                baseTime = currentTime,
                nx = adjustX,
                ny = adjustY
            )
        )
    }

    fun getCurrentPositionLongRainCloud(lon: Double, lat: Double): Single<LongRainCloudVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val nearestCityLocationItemVO = currentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedDateTime = timeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()

                weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = nearestCityLocationItemVO?.longRainCloudParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    fun getCurrentPositionLongTemperature(lon: Double, lat: Double): Single<LongTemperatureVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val nearestCityLocationItemVO = currentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedDateTime = timeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()

                weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = nearestCityLocationItemVO?.longTemperatureParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    fun getCurrentPositionWeatherForecastText(lon: Double, lat: Double): Single<WeatherForecastTextVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val nearestCityLocationItemVO = currentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedDateTime = timeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()

                weatherRepository.getWeatherForecastText(
                    WeatherForecastTextRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        stnId = nearestCityLocationItemVO?.weatherForecastTextParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    fun getCityPositionLiveWeather(city: String): Single<LiveWeatherVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = timeParser.adjustLiveWeatherTime()
                val currentDate = adjustedTime.substring(0, 8).toLong()
                val currentTime = adjustedTime.substring(8).toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 8,
                        dataType = JSON,
                        baseDate = currentDate,
                        baseTime = currentTime,
                        nx = cityLocationItemVO?.longitude?.toLong(),
                        ny = cityLocationItemVO?.latitude?.toLong()
                    )
                )
            }
    }

    fun getCityPositionShortWeather(city: String): Single<ShortWeatherVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = timeParser.adjustLiveWeatherTime()
                val currentDate = adjustedTime.substring(0, 8).toLong()
                val currentTime = adjustedTime.substring(8).toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 290,
                        dataType = JSON,
                        baseDate = currentDate,
                        baseTime = currentTime,
                        nx = cityLocationItemVO?.longitude?.toLong(),
                        ny = cityLocationItemVO?.latitude?.toLong()
                    )
                )
            }
    }

    fun getCityPositionLongRainCloud(city: String): Single<LongRainCloudVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedDateTime = timeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = cityLocationItemVO?.longRainCloudParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    fun getCityPositionLongTemperature(city: String): Single<LongTemperatureVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedDateTime = timeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(
                        serviceKey = "my_service_key",
                        pageNo = 1,
                        numOfRows = 1,
                        dataType = JSON,
                        regId = cityLocationItemVO?.longTemperatureParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    fun getCityPositionWeatherForecastText(city: String): Single<WeatherForecastTextVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedDateTime = timeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getWeatherForecastText(
                    WeatherForecastTextRequestParam(
                        serviceKey = "my_service_key",
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
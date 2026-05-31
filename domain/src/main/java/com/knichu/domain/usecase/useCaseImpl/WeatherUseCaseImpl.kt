package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.param.LiveWeatherRequestParam
import com.knichu.domain.param.LongRainCloudRequestParam
import com.knichu.domain.param.LongTemperatureRequestParam
import com.knichu.domain.param.MidWeatherRequestParam
import com.knichu.domain.param.OpenWeatherRequestParam
import com.knichu.domain.param.ShortWeatherRequestParam
import com.knichu.domain.param.WeatherForecastTextRequestParam
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.repository.OpenWeatherRepository
import com.knichu.domain.repository.WeatherRepository
import com.knichu.domain.useCase.WeatherUseCase
import com.knichu.domain.util.CurrentPositionCityParser
import com.knichu.domain.util.DateTimeParser
import com.knichu.domain.util.LocationParser
import com.knichu.domain.util.SunriseSunsetParser
import com.knichu.domain.util.Weather24HourParser
import com.knichu.domain.util.WeatherNowParser
import com.knichu.domain.util.WeatherOtherInfoParser
import com.knichu.domain.util.WeatherWeeklyParser
import com.knichu.domain.vo.CityLocationVO
import com.knichu.domain.vo.LiveWeatherVO
import com.knichu.domain.vo.OpenWeatherVO
import com.knichu.domain.vo.ShortWeatherVO
import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyVO
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class WeatherUseCaseImpl @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val openWeatherRepository: OpenWeatherRepository,
    private val cityLocationRepository: CityLocationRepository
) : WeatherUseCase {

    @Volatile private var cachedCityLocationVO: CityLocationVO? = null
    @Volatile private var cachedOpenWeatherVO: OpenWeatherVO? = null
    @Volatile private var cachedLiveWeatherVO: LiveWeatherVO? = null
    @Volatile private var cachedShortWeatherVO: ShortWeatherVO? = null

    private suspend fun getCityList(): CityLocationVO =
        cachedCityLocationVO ?: cityLocationRepository.getAllCityLocation().also { cachedCityLocationVO = it }

    private suspend fun getCurrentPositionCity(lon: Double, lat: Double): String {
        val nearestCity = CurrentPositionCityParser.getNearestCity(lon, lat, getCityList())
        return nearestCity?.cityName.toString()
    }

    override fun getNewData() {
        cachedOpenWeatherVO = null
        cachedLiveWeatherVO = null
        cachedShortWeatherVO = null
    }

    override suspend fun getCurrentPositionWeatherNow(lon: Double, lat: Double): WeatherNowVO {
        val adjustedLiveTime = DateTimeParser.adjustLiveWeatherTime()
        val adjustedShortTime = DateTimeParser.adjustShortWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        return coroutineScope {
            val liveDeferred = async {
                cachedLiveWeatherVO ?: weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        baseDate = adjustedLiveTime.substring(0, 8),
                        baseTime = adjustedLiveTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                ).also { cachedLiveWeatherVO = it }
            }
            val shortDeferred = async {
                cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = adjustedShortTime.substring(0, 8),
                        baseTime = adjustedShortTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                ).also { cachedShortWeatherVO = it }
            }
            val openDeferred = async {
                cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(lon = lon, lat = lat)
                ).also { cachedOpenWeatherVO = it }
            }
            val cityDeferred = async { getCurrentPositionCity(lon, lat) }
            WeatherNowParser.getWeatherNowVO(
                liveDeferred.await(), shortDeferred.await(), openDeferred.await(), cityDeferred.await()
            )
        }
    }

    override suspend fun getCurrentPositionWeather24Hour(lon: Double, lat: Double): Weather24HourVO {
        val adjustedTime = DateTimeParser.adjustShortWeatherTime()
        val checkTime = DateTimeParser.checkCurrentShortWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        return coroutineScope {
            val shortDeferred = async {
                weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = adjustedTime.substring(0, 8),
                        baseTime = adjustedTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                )
            }
            val openDeferred = async {
                cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(lon = lon, lat = lat)
                ).also { cachedOpenWeatherVO = it }
            }
            Weather24HourParser.getWeather24HourVO(shortDeferred.await(), openDeferred.await(), checkTime)
        }
    }

    override suspend fun getCurrentPositionWeatherWeekly(lon: Double, lat: Double): WeatherWeeklyVO {
        val cityLocationVO = getCityList()
        val adjustedMidDate = DateTimeParser.adjustShortWeatherTimeWeekly()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val nearestCity = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
        val currentLongForecastTime = DateTimeParser.adjustLongTermForecastTime()
        val currentLongDateTime = currentLongForecastTime.toLong()
        return coroutineScope {
            val midDeferred = async {
                weatherRepository.getMidWeather(
                    MidWeatherRequestParam(baseDate = adjustedMidDate, nx = adjustX, ny = adjustY)
                )
            }
            val longRainDeferred = async {
                weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(regId = nearestCity?.longRainCloudParam, tmFc = currentLongDateTime)
                )
            }
            val longTempDeferred = async {
                weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(regId = nearestCity?.longTemperatureParam, tmFc = currentLongDateTime)
                )
            }
            WeatherWeeklyParser.getWeatherWeeklyVO(
                midDeferred.await(), longRainDeferred.await(), longTempDeferred.await(),
                adjustedMidDate.toString(), currentLongForecastTime
            )
        }
    }

    override suspend fun getCurrentPositionSunriseSunset(lon: Double, lat: Double): SunriseSunsetVO {
        val openWeather = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
            OpenWeatherRequestParam(lon = lon, lat = lat)
        ).also { cachedOpenWeatherVO = it }
        return SunriseSunsetParser.getSunriseSunsetVO(openWeather)
    }

    override suspend fun getCurrentPositionWeatherOtherInfo(lon: Double, lat: Double): WeatherOtherInfoVO {
        val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val liveWeather = cachedLiveWeatherVO ?: weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                baseDate = adjustedTime.substring(0, 8),
                baseTime = adjustedTime.substring(8),
                nx = adjustX, ny = adjustY
            )
        ).also { cachedLiveWeatherVO = it }
        return WeatherOtherInfoParser.getWeatherOtherInfoVO(liveWeather)
    }

    override suspend fun getCurrentPositionWeatherForecastText(lon: Double, lat: Double): WeatherForecastTextVO {
        val nearestCity = CurrentPositionCityParser.getNearestCity(lon, lat, getCityList())
        val currentDateTime = DateTimeParser.adjustLongTermForecastTime().toLong()
        return weatherRepository.getWeatherForecastText(
            WeatherForecastTextRequestParam(stnId = nearestCity?.weatherForecastTextParam, tmFc = currentDateTime)
        )
    }

    override suspend fun getCityPositionWeatherNow(city: String): WeatherNowVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val lon = cityLocationItemVO?.longitude?.toDouble() ?: 126.9778
        val lat = cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
        val adjustedLiveTime = DateTimeParser.adjustLiveWeatherTime()
        val adjustedShortTime = DateTimeParser.adjustShortWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        return coroutineScope {
            val liveDeferred = async {
                weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        baseDate = adjustedLiveTime.substring(0, 8),
                        baseTime = adjustedLiveTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                )
            }
            val shortDeferred = async {
                cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = adjustedShortTime.substring(0, 8),
                        baseTime = adjustedShortTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                ).also { cachedShortWeatherVO = it }
            }
            val openDeferred = async {
                cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(lon = lon, lat = lat)
                ).also { cachedOpenWeatherVO = it }
            }
            WeatherNowParser.getWeatherNowVO(
                liveDeferred.await(), shortDeferred.await(), openDeferred.await(), cityLocationItemVO?.cityName
            )
        }
    }

    override suspend fun getCityPositionWeather24Hour(city: String): Weather24HourVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val lon = cityLocationItemVO?.longitude?.toDouble() ?: 126.9778
        val lat = cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
        val adjustedTime = DateTimeParser.adjustShortWeatherTime()
        val checkTime = DateTimeParser.checkCurrentShortWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        return coroutineScope {
            val shortDeferred = async {
                cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = adjustedTime.substring(0, 8),
                        baseTime = adjustedTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                ).also { cachedShortWeatherVO = it }
            }
            val openDeferred = async {
                cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(lon = lon, lat = lat)
                ).also { cachedOpenWeatherVO = it }
            }
            Weather24HourParser.getWeather24HourVO(shortDeferred.await(), openDeferred.await(), checkTime)
        }
    }

    override suspend fun getCityPositionWeatherWeekly(city: String): WeatherWeeklyVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val lon = cityLocationItemVO?.longitude?.toDouble() ?: 126.9778
        val lat = cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val adjustedMidDate = DateTimeParser.adjustShortWeatherTimeWeekly()
        val currentLongForecastTime = DateTimeParser.adjustLongTermForecastTime()
        val currentLongDateTime = currentLongForecastTime.toLong()
        return coroutineScope {
            val midDeferred = async {
                weatherRepository.getMidWeather(
                    MidWeatherRequestParam(baseDate = adjustedMidDate, nx = adjustX, ny = adjustY)
                )
            }
            val longRainDeferred = async {
                weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(regId = cityLocationItemVO?.longRainCloudParam, tmFc = currentLongDateTime)
                )
            }
            val longTempDeferred = async {
                weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(regId = cityLocationItemVO?.longTemperatureParam, tmFc = currentLongDateTime)
                )
            }
            WeatherWeeklyParser.getWeatherWeeklyVO(
                midDeferred.await(), longRainDeferred.await(), longTempDeferred.await(),
                adjustedMidDate.toString(), currentLongForecastTime
            )
        }
    }

    override suspend fun getCityPositionWeatherOtherInfo(city: String): WeatherOtherInfoVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val lon = cityLocationItemVO?.longitude?.toDouble() ?: 126.9778
        val lat = cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
        val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val liveWeather = weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                baseDate = adjustedTime.substring(0, 8),
                baseTime = adjustedTime.substring(8),
                nx = adjustX, ny = adjustY
            )
        )
        return WeatherOtherInfoParser.getWeatherOtherInfoVO(liveWeather)
    }

    override suspend fun getCityPositionSunriseSunset(city: String): SunriseSunsetVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val openWeather = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
            OpenWeatherRequestParam(
                lon = cityLocationItemVO?.longitude?.toDouble(),
                lat = cityLocationItemVO?.latitude?.toDouble()
            )
        ).also { cachedOpenWeatherVO = it }
        return SunriseSunsetParser.getSunriseSunsetVO(openWeather)
    }

    override suspend fun getCityPositionWeatherForecastText(city: String): WeatherForecastTextVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val currentDateTime = DateTimeParser.adjustLongTermForecastTime().toLong()
        return weatherRepository.getWeatherForecastText(
            WeatherForecastTextRequestParam(stnId = cityLocationItemVO?.weatherForecastTextParam, tmFc = currentDateTime)
        )
    }

    override suspend fun getStoredCityListWeatherNow(city: String): WeatherNowCityListItemVO {
        val cityLocationItemVO = getCityList().item?.find { it.cityName == city }
        val lon = cityLocationItemVO?.longitude?.toDouble() ?: 126.9778
        val lat = cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
        val adjustedLiveTime = DateTimeParser.adjustLiveWeatherTime()
        val adjustedShortTime = DateTimeParser.adjustShortWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        return coroutineScope {
            val liveDeferred = async {
                weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        baseDate = adjustedLiveTime.substring(0, 8),
                        baseTime = adjustedLiveTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                )
            }
            val shortDeferred = async {
                cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = adjustedShortTime.substring(0, 8),
                        baseTime = adjustedShortTime.substring(8),
                        nx = adjustX, ny = adjustY
                    )
                )
            }
            val openDeferred = async {
                cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(lon = lon, lat = lat)
                )
            }
            WeatherNowParser.getWeatherNowCityListItemVO(
                liveDeferred.await(), shortDeferred.await(), openDeferred.await(), cityLocationItemVO?.cityName
            )
        }
    }
}

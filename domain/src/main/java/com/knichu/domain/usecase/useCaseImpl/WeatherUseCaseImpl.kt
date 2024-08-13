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
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class WeatherUseCaseImpl @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val openWeatherRepository: OpenWeatherRepository,
    private val cityLocationRepository: CityLocationRepository
): WeatherUseCase {
    private val allCityList = cityLocationRepository.getAllCityLocation()
    private var cachedOpenWeatherVO: Single<OpenWeatherVO>? = null
    private var cachedLiveWeatherVO: Single<LiveWeatherVO>? = null
    private var cachedShortWeatherVO: Single<ShortWeatherVO>? = null

    private fun getCurrentPositionCity(lon: Double, lat: Double): Single<String> {
        return allCityList
            .map { cityLocationVO ->
                val nearestCityLocationItemVO = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                nearestCityLocationItemVO?.cityName.toString()
            }
    }

    override fun getNewData() {
        cachedOpenWeatherVO = null
        cachedLiveWeatherVO = null
        cachedShortWeatherVO = null
    }

    override fun getCurrentPositionWeatherNow(lon: Double, lat: Double): Single<WeatherNowVO> {
        val adjustedLiveTime = DateTimeParser.adjustLiveWeatherTime()
        val currentLiveDate = adjustedLiveTime.substring(0, 8)
        val currentLiveTime = adjustedLiveTime.substring(8)
        val adjustedShortTime = DateTimeParser.adjustShortWeatherTime()
        val currentShortDate = adjustedShortTime.substring(0, 8)
        val currentShortTime = adjustedShortTime.substring(8)
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val liveWeatherVO = cachedLiveWeatherVO ?: weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                baseDate = currentLiveDate,
                baseTime = currentLiveTime, // 변경 가능
                nx = adjustX,
                ny = adjustY
            )
        ).doOnSuccess { cachedLiveWeatherVO = Single.just(it) }
        val shortWeatherVO = cachedShortWeatherVO ?: weatherRepository.getShortWeather(
            ShortWeatherRequestParam(
                baseDate = currentShortDate,
                baseTime = currentShortTime,
                nx = adjustX,
                ny = adjustY
            )
        ).doOnSuccess {cachedShortWeatherVO = Single.just(it) }

        val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
            OpenWeatherRequestParam(
                lon = lon,
                lat = lat,
            )
        ).doOnSuccess { cachedOpenWeatherVO = Single.just(it) }

        val currentPositionCity = getCurrentPositionCity(lon, lat)
        return WeatherNowParser.getWeatherNowVO(liveWeatherVO, shortWeatherVO, openWeatherVO, currentPositionCity)
    }

    override fun getCurrentPositionWeather24Hour(lon: Double, lat: Double): Single<Weather24HourVO> {
        val adjustedTime = DateTimeParser.adjustShortWeatherTime()
        val currentDate = adjustedTime.substring(0, 8)
        val currentTime = adjustedTime.substring(8)
        val checkTime = DateTimeParser.checkCurrentShortWeatherTime()
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val shortWeatherVO = weatherRepository.getShortWeather(
            ShortWeatherRequestParam(
                baseDate = currentDate,
                baseTime = currentTime,
                nx = adjustX,
                ny = adjustY
            )
        )

        val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
            OpenWeatherRequestParam(
                lon = lon,
                lat = lat,
            )
        ).doOnSuccess { cachedOpenWeatherVO = Single.just(it) }

        return Weather24HourParser.getWeather24HourVO(shortWeatherVO, openWeatherVO, checkTime)
    }

    override fun getCurrentPositionWeatherWeekly(lon: Double, lat: Double): Single<WeatherWeeklyVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedMidTime = DateTimeParser.adjustShortWeatherTimeWeekly()
                val adjustedMidDate = adjustedMidTime
                val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
                val midWeatherVO = weatherRepository.getMidWeather(
                    MidWeatherRequestParam(
                        baseDate = adjustedMidDate,
                        nx = adjustX,
                        ny = adjustY
                    )
                )

                val nearestCityLocationItemVO = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedLongDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentLongDateTime = adjustedLongDateTime.toLong()
                val longRainCloudVO = weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(
                        regId = nearestCityLocationItemVO?.longRainCloudParam,
                        tmFc = currentLongDateTime
                    )
                )

                val longTemperatureVO = weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(
                        regId = nearestCityLocationItemVO?.longTemperatureParam,
                        tmFc = currentLongDateTime
                    )
                )

                WeatherWeeklyParser.getWeatherWeeklyVO(
                    midWeatherVO,
                    longRainCloudVO,
                    longTemperatureVO,
                    adjustedMidDate.toString()
                )
            }
    }

    override fun getCurrentPositionSunriseSunset(lon: Double, lat: Double): Single<SunriseSunsetVO> {
        val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
            OpenWeatherRequestParam(
                lon = lon,
                lat = lat,
            )
        ).doOnSuccess { cachedOpenWeatherVO = Single.just(it) }
        return SunriseSunsetParser.getSunriseSunsetVO(openWeatherVO)
    }

    override fun getCurrentPositionWeatherOtherInfo(lon: Double, lat: Double): Single<WeatherOtherInfoVO> {
        val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
        val currentDate = adjustedTime.substring(0, 8)
        val currentTime = adjustedTime.substring(8)
        val (adjustX, adjustY) = LocationParser.getNxNy(lon, lat)
        val liveWeatherVO = cachedLiveWeatherVO ?: weatherRepository.getLiveWeather(
            LiveWeatherRequestParam(
                baseDate = currentDate,
                baseTime = currentTime, // 변경 가능
                nx = adjustX,
                ny = adjustY
            )
        ).doOnSuccess { cachedLiveWeatherVO = Single.just(it) }
        return WeatherOtherInfoParser.getWeatherOtherInfoVO(liveWeatherVO)
    }

    override fun getCurrentPositionWeatherForecastText(lon: Double, lat: Double): Single<WeatherForecastTextVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val nearestCityLocationItemVO = CurrentPositionCityParser.getNearestCity(lon, lat, cityLocationVO)
                val adjustedDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()

                weatherRepository.getWeatherForecastText(
                    WeatherForecastTextRequestParam(
                        stnId = nearestCityLocationItemVO?.weatherForecastTextParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    override fun getCityPositionWeatherNow(city: String): Single<WeatherNowVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedLiveTime = DateTimeParser.adjustLiveWeatherTime()
                val currentLiveDate = adjustedLiveTime.substring(0, 8)
                val currentLiveTime = adjustedLiveTime.substring(8)
                val adjustedShortTime = DateTimeParser.adjustShortWeatherTime()
                val currentShortDate = adjustedShortTime.substring(0, 8)
                val currentShortTime = adjustedShortTime.substring(8)

                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val (adjustX, adjustY) = LocationParser.getNxNy(
                    cityLocationItemVO?.longitude?.toDouble() ?: 126.9778,
                    cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
                )
                val liveWeatherVO = weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        baseDate = currentLiveDate,
                        baseTime = currentLiveTime,
                        nx = adjustX,
                        ny = adjustY
                    )
                )

                val shortWeatherVO = cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = currentShortDate,
                        baseTime = currentShortTime,
                        nx = adjustX,
                        ny = adjustY
                    )
                ).doOnSuccess {cachedShortWeatherVO = Single.just(it) }

                val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble(),
                    )
                ).doOnSuccess { cachedOpenWeatherVO = Single.just(it) }

                WeatherNowParser.getWeatherNowVO(liveWeatherVO, shortWeatherVO, openWeatherVO, cityLocationItemVO?.cityName)
            }
    }

    override fun getCityPositionWeather24Hour(city: String): Single<Weather24HourVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = DateTimeParser.adjustShortWeatherTime()
                val currentDate = adjustedTime.substring(0, 8)
                val currentTime = adjustedTime.substring(8)
                val checkTime = DateTimeParser.checkCurrentShortWeatherTime()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val (adjustX, adjustY) = LocationParser.getNxNy(
                    cityLocationItemVO?.longitude?.toDouble() ?: 126.9778,
                    cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
                )
                val shortWeatherVO = cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = currentDate,
                        baseTime = currentTime,
                        nx = adjustX,
                        ny = adjustY
                    )
                ).doOnSuccess {cachedShortWeatherVO = Single.just(it) }

                val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble(),
                    )
                ).doOnSuccess { cachedOpenWeatherVO = Single.just(it) }

                Weather24HourParser.getWeather24HourVO(shortWeatherVO, openWeatherVO, checkTime)
            }
    }

    override fun getCityPositionWeatherWeekly(city: String): Single<WeatherWeeklyVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val (adjustX, adjustY) = LocationParser.getNxNy(
                    cityLocationItemVO?.longitude?.toDouble() ?: 126.9778,
                    cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
                )
                val adjustedMidTime = DateTimeParser.adjustShortWeatherTimeWeekly()
                val adjustedMidDate = adjustedMidTime
                val midWeatherVO = weatherRepository.getMidWeather(
                    MidWeatherRequestParam(
                        baseDate = adjustedMidDate,
                        nx = adjustX,
                        ny = adjustY
                    )
                )

                val adjustedLongDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentLongDateTime = adjustedLongDateTime.toLong()
                val longRainCloudVO = weatherRepository.getLongRainCloud(
                    LongRainCloudRequestParam(
                        regId = cityLocationItemVO?.longRainCloudParam,
                        tmFc = currentLongDateTime
                    )
                )

                val longTemperatureVO = weatherRepository.getLongTemperature(
                    LongTemperatureRequestParam(
                        regId = cityLocationItemVO?.longTemperatureParam,
                        tmFc = currentLongDateTime
                    )
                )

                WeatherWeeklyParser.getWeatherWeeklyVO(
                    midWeatherVO,
                    longRainCloudVO,
                    longTemperatureVO,
                    adjustedMidDate.toString()
                )
            }
    }

    override fun getCityPositionWeatherOtherInfo(city: String): Single<WeatherOtherInfoVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedTime = DateTimeParser.adjustLiveWeatherTime()
                val currentDate = adjustedTime.substring(0, 8)
                val currentTime = adjustedTime.substring(8)
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val (adjustX, adjustY) = LocationParser.getNxNy(
                    cityLocationItemVO?.longitude?.toDouble() ?: 126.9778,
                    cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
                )
                val liveWeatherVO = weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        baseDate = currentDate,
                        baseTime = currentTime, // 변경 가능
                        nx = adjustX,
                        ny = adjustY
                    )
                )
                WeatherOtherInfoParser.getWeatherOtherInfoVO(liveWeatherVO)
            }
    }

    override fun getCityPositionSunriseSunset(city: String): Single<SunriseSunsetVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble(),
                    )
                ).doOnSuccess { cachedOpenWeatherVO = Single.just(it) }
                SunriseSunsetParser.getSunriseSunsetVO(openWeatherVO)
            }
    }

    override fun getCityPositionWeatherForecastText(city: String): Single<WeatherForecastTextVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedDateTime = DateTimeParser.adjustLongTermForecastTime()
                val currentDateTime = adjustedDateTime.toLong()
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }

                weatherRepository.getWeatherForecastText(
                    WeatherForecastTextRequestParam(
                        stnId = cityLocationItemVO?.weatherForecastTextParam,
                        tmFc = currentDateTime
                    )
                )
            }
    }

    override fun getStoredCityListWeatherNow(city: String): Single<WeatherNowCityListItemVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                val adjustedLiveTime = DateTimeParser.adjustLiveWeatherTime()
                val currentLiveDate = adjustedLiveTime.substring(0, 8)
                val currentLiveTime = adjustedLiveTime.substring(8)
                val adjustedShortTime = DateTimeParser.adjustShortWeatherTime()
                val currentShortDate = adjustedShortTime.substring(0, 8)
                val currentShortTime = adjustedShortTime.substring(8)
                val cityLocationItemVO = cityLocationVO.item?.find { it.cityName == city }
                val (adjustX, adjustY) = LocationParser.getNxNy(
                    cityLocationItemVO?.longitude?.toDouble() ?: 126.9778,
                    cityLocationItemVO?.latitude?.toDouble() ?: 37.5683
                )

                val liveWeatherVO = weatherRepository.getLiveWeather(
                    LiveWeatherRequestParam(
                        baseDate = currentLiveDate,
                        baseTime = currentLiveTime,
                        nx = adjustX,
                        ny = adjustY
                    )
                )

                val shortWeatherVO = cachedShortWeatherVO ?: weatherRepository.getShortWeather(
                    ShortWeatherRequestParam(
                        baseDate = currentShortDate,
                        baseTime = currentShortTime,
                        nx = adjustX,
                        ny = adjustY
                    )
                )

                val openWeatherVO = cachedOpenWeatherVO ?: openWeatherRepository.getOpenWeather(
                    OpenWeatherRequestParam(
                        lon = cityLocationItemVO?.longitude?.toDouble(),
                        lat = cityLocationItemVO?.latitude?.toDouble(),
                    )
                )

                WeatherNowParser.getWeatherNowCityListItemVO(
                    liveWeatherVO,
                    shortWeatherVO,
                    openWeatherVO,
                    cityLocationItemVO?.cityName
                )
            }
    }
}
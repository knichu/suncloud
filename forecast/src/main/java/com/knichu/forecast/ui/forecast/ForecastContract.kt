package com.knichu.forecast.ui.forecast

import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyItemVO

data class ForecastUiState(
    val tempUnit: WeatherTempUnit = WeatherTempUnit.CELSIUS,
    val isLoading: Boolean = false,
    val isDrawerOpen: Boolean = false,
    val selectedCity: String? = null,           // null = 현재 위치
    val lonLat: Pair<Double, Double>? = null,
    val weatherNow: WeatherNowVO? = null,
    val weather24Hour: List<Weather24HourItemVO> = emptyList(),
    val weatherWeekly: List<WeatherWeeklyItemVO> = emptyList(),
    val sunriseSunset: SunriseSunsetVO? = null,
    val weatherOtherInfo: WeatherOtherInfoVO? = null,
    val weatherForecastText: WeatherForecastTextVO? = null,
    val airPollution: AirPollutionDataVO? = null,
    val currentPositionCity: WeatherNowVO? = null,
    val storedCityList: List<WeatherNowCityListItemVO> = emptyList(),
    val scrollToTopTrigger: Long = 0L,
    val error: String? = null
)

sealed class ForecastUiIntent {
    data class UpdateLocation(val lon: Double, val lat: Double) : ForecastUiIntent()
    data class SelectCity(val city: String) : ForecastUiIntent()
    object SelectCurrentPosition : ForecastUiIntent()
    object Refresh : ForecastUiIntent()
    object OpenDrawer : ForecastUiIntent()
    object CloseDrawer : ForecastUiIntent()
    object NavigateToCitySearch : ForecastUiIntent()
    object NavigateToCityManage : ForecastUiIntent()
    object ScrollToTop : ForecastUiIntent()
    object ExitApp : ForecastUiIntent()
}

sealed class ForecastUiEffect {
    object NavigateToCitySearch : ForecastUiEffect()
    object NavigateToCityManage : ForecastUiEffect()
    object ExitApp : ForecastUiEffect()
}

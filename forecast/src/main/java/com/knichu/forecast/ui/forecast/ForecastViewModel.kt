package com.knichu.forecast.ui.forecast

import androidx.lifecycle.viewModelScope
import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.WeatherUseCase
import com.knichu.domain.vo.WeatherNowCityListItemVO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val airPollutionUseCase: AirPollutionUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseMviViewModel<ForecastUiIntent, ForecastUiState, ForecastUiEffect>() {

    override fun initialState() = ForecastUiState()

    init {
        observeCityList()
    }

    override fun handleIntent(intent: ForecastUiIntent) {
        when (intent) {
            is ForecastUiIntent.UpdateLocation -> updateLocation(intent.lon, intent.lat)
            is ForecastUiIntent.SelectCity     -> selectCity(intent.city)
            is ForecastUiIntent.SelectCurrentPosition -> selectCurrentPosition()
            is ForecastUiIntent.Refresh        -> refresh()
            is ForecastUiIntent.OpenDrawer     -> setState { copy(isDrawerOpen = true) }
            is ForecastUiIntent.CloseDrawer    -> setState { copy(isDrawerOpen = false) }
            is ForecastUiIntent.NavigateToCitySearch -> sendEffect(ForecastUiEffect.NavigateToCitySearch)
            is ForecastUiIntent.NavigateToCityManage -> sendEffect(ForecastUiEffect.NavigateToCityManage)
            is ForecastUiIntent.ScrollToTop    -> setState { copy(scrollToTopTrigger = System.currentTimeMillis()) }
            is ForecastUiIntent.ExitApp        -> sendEffect(ForecastUiEffect.ExitApp)
        }
    }

    private fun updateLocation(lon: Double, lat: Double) {
        if (uiState.value.lonLat == Pair(lon, lat)) return
        setState { copy(lonLat = Pair(lon, lat), selectedCity = null) }
        fetchCurrentPositionCity(lon, lat)
        fetchAllData()
    }

    private fun selectCity(city: String) {
        setState { copy(selectedCity = city, isDrawerOpen = false) }
        fetchAllData()
    }

    private fun selectCurrentPosition() {
        setState { copy(selectedCity = null, isDrawerOpen = false) }
        fetchAllData()
    }

    private fun refresh() {
        weatherUseCase.getNewData()
        fetchAllData()
    }

    private fun fetchAllData() {
        val state = uiState.value
        if (state.selectedCity != null) {
            fetchCityWeatherData(state.selectedCity)
        } else {
            val (lon, lat) = state.lonLat ?: Pair(DEFAULT_LON, DEFAULT_LAT)
            fetchCurrentPositionWeatherData(lon, lat)
        }
    }

    private fun fetchCurrentPositionWeatherData(lon: Double, lat: Double) {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            runCatching {
                coroutineScope {
                    val weatherNowD         = async { weatherUseCase.getCurrentPositionWeatherNow(lon, lat) }
                    val weather24HourD      = async { weatherUseCase.getCurrentPositionWeather24Hour(lon, lat) }
                    val weatherWeeklyD      = async { weatherUseCase.getCurrentPositionWeatherWeekly(lon, lat) }
                    val sunriseSunsetD      = async { weatherUseCase.getCurrentPositionSunriseSunset(lon, lat) }
                    val weatherOtherInfoD   = async { weatherUseCase.getCurrentPositionWeatherOtherInfo(lon, lat) }
                    val weatherForecastTextD = async { weatherUseCase.getCurrentPositionWeatherForecastText(lon, lat) }
                    val airPollutionD       = async { airPollutionUseCase.getCurrentPositionAirPollution(lon, lat) }

                    val weatherNow          = weatherNowD.await()
                    val weather24Hour       = weather24HourD.await()
                    val weatherWeekly       = weatherWeeklyD.await()
                    val sunriseSunset       = sunriseSunsetD.await()
                    val weatherOtherInfo    = weatherOtherInfoD.await()
                    val weatherForecastText = weatherForecastTextD.await()
                    val airPollution        = airPollutionD.await()

                    setState {
                        copy(
                            isLoading           = false,
                            weatherNow          = weatherNow,
                            weather24Hour       = weather24Hour.item ?: emptyList(),
                            weatherWeekly       = weatherWeekly.item ?: emptyList(),
                            sunriseSunset       = sunriseSunset,
                            weatherOtherInfo    = weatherOtherInfo,
                            weatherForecastText = weatherForecastText,
                            airPollution        = airPollution
                        )
                    }
                }
            }.onFailure { e ->
                setState { copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun fetchCityWeatherData(city: String) {
        viewModelScope.launch {
            setState { copy(isLoading = true, error = null) }
            runCatching {
                coroutineScope {
                    val weatherNowD         = async { weatherUseCase.getCityPositionWeatherNow(city) }
                    val weather24HourD      = async { weatherUseCase.getCityPositionWeather24Hour(city) }
                    val weatherWeeklyD      = async { weatherUseCase.getCityPositionWeatherWeekly(city) }
                    val sunriseSunsetD      = async { weatherUseCase.getCityPositionSunriseSunset(city) }
                    val weatherOtherInfoD   = async { weatherUseCase.getCityPositionWeatherOtherInfo(city) }
                    val weatherForecastTextD = async { weatherUseCase.getCityPositionWeatherForecastText(city) }
                    val airPollutionD       = async { airPollutionUseCase.getCityPositionAirPollution(city) }

                    val weatherNow          = weatherNowD.await()
                    val weather24Hour       = weather24HourD.await()
                    val weatherWeekly       = weatherWeeklyD.await()
                    val sunriseSunset       = sunriseSunsetD.await()
                    val weatherOtherInfo    = weatherOtherInfoD.await()
                    val weatherForecastText = weatherForecastTextD.await()
                    val airPollution        = airPollutionD.await()

                    setState {
                        copy(
                            isLoading           = false,
                            weatherNow          = weatherNow,
                            weather24Hour       = weather24Hour.item ?: emptyList(),
                            weatherWeekly       = weatherWeekly.item ?: emptyList(),
                            sunriseSunset       = sunriseSunset,
                            weatherOtherInfo    = weatherOtherInfo,
                            weatherForecastText = weatherForecastText,
                            airPollution        = airPollution
                        )
                    }
                }
            }.onFailure { e ->
                setState { copy(isLoading = false, error = e.message) }
            }
        }
    }

    private fun fetchCurrentPositionCity(lon: Double, lat: Double) {
        viewModelScope.launch {
            runCatching { weatherUseCase.getCurrentPositionWeatherNow(lon, lat) }
                .onSuccess { result -> setState { copy(currentPositionCity = result) } }
        }
    }

    private fun observeCityList() {
        viewModelScope.launch {
            dataStoreUseCase.getCityList().collect { cityList ->
                val currentList = uiState.value.storedCityList
                val currentCityNames = currentList.map { it.city }
                val newCities = cityList.filter { it !in currentCityNames }
                val removedCities = currentCityNames.filter { it !in cityList }

                if (removedCities.isNotEmpty()) {
                    setState { copy(storedCityList = storedCityList.filter { it.city !in removedCities }) }
                }

                if (newCities.isNotEmpty()) {
                    val newItems = newCities.mapNotNull { city ->
                        runCatching { weatherUseCase.getStoredCityListWeatherNow(city) }.getOrNull()
                    }
                    if (newItems.isNotEmpty()) {
                        setState { copy(storedCityList = storedCityList + newItems) }
                    }
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_LON = 126.9778
        private const val DEFAULT_LAT = 37.5683
    }
}

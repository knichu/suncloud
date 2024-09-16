package com.knichu.forecast.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knichu.common.base.BaseViewModel
import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.WeatherUseCase
import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.AirPollutionVO
import com.knichu.domain.vo.SunriseSunsetVO
import com.knichu.domain.vo.Weather24HourItemVO
import com.knichu.domain.vo.WeatherForecastTextVO
import com.knichu.domain.vo.WeatherNowCityListItemVO
import com.knichu.domain.vo.WeatherNowVO
import com.knichu.domain.vo.WeatherOtherInfoVO
import com.knichu.domain.vo.WeatherWeeklyItemVO
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(
    private val airPollutionUseCase: AirPollutionUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseViewModel() {
    private val _selectedCity: MutableLiveData<String> = MutableLiveData()
    val selectedCity: LiveData<String> = _selectedCity

    private val _isCityChecked: MutableLiveData<Boolean> = MutableLiveData(false)
    val isCityChecked: LiveData<Boolean> = _isCityChecked

    private val _currentPositionCityDataAtSlideView: MutableLiveData<WeatherNowVO> = MutableLiveData()
    val currentPositionCityDataAtSlideView: LiveData<WeatherNowVO> = _currentPositionCityDataAtSlideView

    private val _storedCityListNowData: MutableLiveData<List<WeatherNowCityListItemVO>> = MutableLiveData()
    val storedCityListNowData: LiveData<List<WeatherNowCityListItemVO>> = _storedCityListNowData

    private val _lonLat: MutableLiveData<Pair<Double, Double>> = MutableLiveData()
    val lonLat: LiveData<Pair<Double, Double>> = _lonLat

    private val _weatherNowData: MutableLiveData<WeatherNowVO> = MutableLiveData()
    val weatherNowData: LiveData<WeatherNowVO> = _weatherNowData

    private val _weather24HourData: MutableLiveData<List<Weather24HourItemVO>> = MutableLiveData()
    val weather24HourData: LiveData<List<Weather24HourItemVO>> = _weather24HourData

    private val _weatherWeeklyData: MutableLiveData<List<WeatherWeeklyItemVO>> = MutableLiveData()
    val weatherWeeklyData: LiveData<List<WeatherWeeklyItemVO>> = _weatherWeeklyData

    private val _sunriseSunsetData: MutableLiveData<SunriseSunsetVO> = MutableLiveData()
    val sunriseSunsetData: LiveData<SunriseSunsetVO> = _sunriseSunsetData

    private val _weatherOtherInfoData: MutableLiveData<WeatherOtherInfoVO> = MutableLiveData()
    val weatherOtherInfoData: LiveData<WeatherOtherInfoVO> = _weatherOtherInfoData

    private val _weatherForecastTextData: MutableLiveData<WeatherForecastTextVO> = MutableLiveData()
    val weatherForecastTextData: LiveData<WeatherForecastTextVO> = _weatherForecastTextData

    private val _airPollutionData: MutableLiveData<AirPollutionDataVO> = MutableLiveData()
    val airPollutionData: LiveData<AirPollutionDataVO> = _airPollutionData

    private val _isAppBarExpanded: MutableLiveData<Boolean> = MutableLiveData()
    val isAppBarExpanded: LiveData<Boolean> = _isAppBarExpanded

    private val _isRefreshing: MutableLiveData<Boolean> = MutableLiveData()
    val isRefreshing: LiveData<Boolean> = _isRefreshing

    init {
        observeCityList()
    }
    fun setAppBarExpanded(isExpanded: Boolean) {
        _isAppBarExpanded.value = isExpanded
    }

    fun setRefreshing(isRefreshing: Boolean) {
        _isRefreshing.value = isRefreshing
    }

    fun changeCityCheckFalse() {
        _isCityChecked.value = false
        _selectedCity.value = String()
        fetchData()
    }

    fun updateLocationFetchData(lon: Double, lat: Double) {
        _lonLat.value = Pair(lon, lat)
        fetchCurrentPositionCity()
        fetchData()
    }

    fun updateSelectedCityFetchData(city: String) {
        _selectedCity.value = city
        _isCityChecked.value = true
        fetchData()
    }

    private fun fetchData() {
        if (_isCityChecked.value == true) {
            weatherUseCase.getNewData()
            fetchCityNowData(_selectedCity.value?: DEFAULT_CITY)
            fetchCity24HourData(_selectedCity.value?: DEFAULT_CITY)
            fetchCityWeeklyData(_selectedCity.value?: DEFAULT_CITY)
            fetchCitySunriseSunsetData(_selectedCity.value?: DEFAULT_CITY)
            fetchCityOtherInfoData(_selectedCity.value?: DEFAULT_CITY)
            fetchCityForecastTextData(_selectedCity.value?: DEFAULT_CITY)
            fetchCityAirPollutionData(_selectedCity.value?: DEFAULT_CITY)
        } else {
            weatherUseCase.getNewData()
            fetchCurrentNowData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
            fetchCurrent24HourData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
            fetchCurrentWeeklyData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
            fetchCurrentSunriseSunsetData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
            fetchCurrentOtherInfoData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
            fetchCurrentForecastTextData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
            fetchCurrentAirPollutionData(_lonLat.value?: Pair(DEFAULT_LON, DEFAULT_LAT))
        }
    }

    private fun fetchCurrentNowData(lonLat: Pair<Double, Double>) {
        weatherUseCase.getCurrentPositionWeatherNow(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .bind(_weatherNowData)
    }

    private fun fetchCurrent24HourData(lonLat: Pair<Double, Double>) {
        weatherUseCase.getCurrentPositionWeather24Hour(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .map{ it.item?: emptyList() }
            .bind(_weather24HourData)
    }

    private fun fetchCurrentWeeklyData(lonLat: Pair<Double, Double>) {
        weatherUseCase.getCurrentPositionWeatherWeekly(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .map{ it.item?: emptyList() }
            .bind(_weatherWeeklyData)
    }

    private fun fetchCurrentSunriseSunsetData(lonLat: Pair<Double, Double>) {
        weatherUseCase.getCurrentPositionSunriseSunset(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .bind(_sunriseSunsetData)
    }

    private fun fetchCurrentOtherInfoData(lonLat: Pair<Double, Double>) {
        weatherUseCase.getCurrentPositionWeatherOtherInfo(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .bind(_weatherOtherInfoData)
    }

    private fun fetchCurrentForecastTextData(lonLat: Pair<Double, Double>) {
        weatherUseCase.getCurrentPositionWeatherForecastText(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .bind(_weatherForecastTextData)
    }

    private fun fetchCurrentAirPollutionData(lonLat: Pair<Double, Double>) {
        airPollutionUseCase.getCurrentPositionAirPollution(lonLat.first, lonLat.second)
            .applySchedulers()
            .toObservable()
            .bind(_airPollutionData)
    }

    private fun fetchCityNowData(city: String) {
        weatherUseCase.getCityPositionWeatherNow(city)
            .applySchedulers()
            .toObservable()
            .bind(_weatherNowData)
    }

    private fun fetchCity24HourData(city: String) {
        weatherUseCase.getCityPositionWeather24Hour(city)
            .applySchedulers()
            .toObservable()
            .map{ it.item?: emptyList() }
            .bind(_weather24HourData)
    }

    private fun fetchCityWeeklyData(city: String) {
        weatherUseCase.getCityPositionWeatherWeekly(city)
            .applySchedulers()
            .toObservable()
            .map{ it.item?: emptyList() }
            .bind(_weatherWeeklyData)
    }

    private fun fetchCitySunriseSunsetData(city: String) {
        weatherUseCase.getCityPositionSunriseSunset(city)
            .applySchedulers()
            .toObservable()
            .bind(_sunriseSunsetData)
    }

    private fun fetchCityOtherInfoData(city: String) {
        weatherUseCase.getCityPositionWeatherOtherInfo(city)
            .applySchedulers()
            .toObservable()
            .bind(_weatherOtherInfoData)
    }

    private fun fetchCityForecastTextData(city: String) {
        weatherUseCase.getCityPositionWeatherForecastText(city)
            .applySchedulers()
            .toObservable()
            .bind(_weatherForecastTextData)
    }

    private fun fetchCityAirPollutionData(city: String) {
        airPollutionUseCase.getCityPositionAirPollution(city)
            .applySchedulers()
            .toObservable()
            .bind(_airPollutionData)
    }

    private fun observeCityList() {
        dataStoreUseCase.getCityList()
            .applySchedulers()
            .toObservable()
            .subscribe { newCityList ->
                val currentList = _storedCityListNowData.value.orEmpty()
                val currentCityNames = currentList.map { it.city }
                val newCities = newCityList.filter { it !in currentCityNames }
                val removedCities = currentCityNames.filter { it !in newCityList }

                if (newCities.isNotEmpty()) {
                    Observable.fromIterable(newCities)
                        .concatMap { city ->
                            weatherUseCase.getStoredCityListWeatherNow(city)
                                .toObservable()
                        }
                        .toList()
                        .toObservable()
                        .applySchedulers()
                        .subscribe { newCityWeatherList ->
                            val updatedList = currentList.toMutableList()
                            updatedList.addAll(newCityWeatherList)
                            _storedCityListNowData.value = updatedList
                        }.let(compositeDisposable::add)
                }

                val updatedList = currentList.filter { it.city !in removedCities }
                _storedCityListNowData.value = updatedList
            }.let(compositeDisposable::add)
    }

    private fun fetchCurrentPositionCity() {
        weatherUseCase.getCurrentPositionWeatherNow(
            _lonLat.value?.first ?: 126.9778,
            _lonLat.value?.second ?: 37.5683
        )
            .applySchedulers()
            .toObservable()
            .bind(_currentPositionCityDataAtSlideView)
    }

    companion object {
        private const val DEFAULT_CITY = "서울"
        private const val DEFAULT_LON = 126.9778
        private const val DEFAULT_LAT = 37.5683
    }

}
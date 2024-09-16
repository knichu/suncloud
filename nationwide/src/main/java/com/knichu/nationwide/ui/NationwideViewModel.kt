package com.knichu.nationwide.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.knichu.common.base.BaseViewModel
import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.WeatherUseCase
import com.knichu.domain.vo.WeatherForecastTextVO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NationwideViewModel @Inject constructor(
    private val airPollutionUseCase: AirPollutionUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseViewModel() {
    private val _data: MutableLiveData<String> = MutableLiveData()
    val data: LiveData<String> = _data
}
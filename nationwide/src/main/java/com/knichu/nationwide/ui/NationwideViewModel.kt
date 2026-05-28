package com.knichu.nationwide.ui

import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.useCase.AirPollutionUseCase
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NationwideViewModel @Inject constructor(
    private val airPollutionUseCase: AirPollutionUseCase,
    private val weatherUseCase: WeatherUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseMviViewModel<NationwideUiIntent, NationwideUiState, NationwideUiEffect>() {

    override fun initialState() = NationwideUiState()

    override fun handleIntent(intent: NationwideUiIntent) {}
}

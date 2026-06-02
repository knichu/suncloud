package com.knichu.setting.ui

import androidx.lifecycle.viewModelScope
import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.constants.WeatherTempUnit
import com.knichu.domain.useCase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase
) : BaseMviViewModel<SettingUiIntent, SettingUiState, SettingUiEffect>() {

    override fun initialState() = SettingUiState()

    init {
        loadTempUnit()
    }

    override fun handleIntent(intent: SettingUiIntent) {
        when (intent) {
            is SettingUiIntent.OnTempUnitChanged -> saveTempUnit(intent.unit)
        }
    }

    private fun loadTempUnit() {
        viewModelScope.launch {
            val unit = dataStoreUseCase.getUserTempUnit()
            setState { copy(tempUnit = if (unit == WeatherTempUnit.NONE) WeatherTempUnit.CELSIUS else unit) }
        }
    }

    private fun saveTempUnit(unit: WeatherTempUnit) {
        setState { copy(tempUnit = unit) }
        viewModelScope.launch {
            dataStoreUseCase.storeUserTempUnit(unit)
        }
    }
}

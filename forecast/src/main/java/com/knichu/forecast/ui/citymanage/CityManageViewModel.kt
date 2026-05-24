package com.knichu.forecast.ui.citymanage

import androidx.lifecycle.viewModelScope
import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.useCase.DataStoreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityManageViewModel @Inject constructor(
    private val dataStoreUseCase: DataStoreUseCase
) : BaseMviViewModel<CityManageUiIntent, CityManageUiState, CityManageUiEffect>() {

    override fun initialState() = CityManageUiState()

    init {
        observeCityList()
    }

    override fun handleIntent(intent: CityManageUiIntent) {
        when (intent) {
            is CityManageUiIntent.ToggleSelectionMode    -> toggleSelectionMode()
            is CityManageUiIntent.ToggleCitySelection    -> toggleCitySelection(intent.city)
            is CityManageUiIntent.DeleteSelectedCities   -> deleteSelectedCities()
            is CityManageUiIntent.NavigateBack           -> sendEffect(CityManageUiEffect.NavigateBack)
        }
    }

    private fun observeCityList() {
        viewModelScope.launch {
            dataStoreUseCase.getCityList().collect { cityList ->
                setState { copy(cityList = cityList) }
            }
        }
    }

    private fun toggleSelectionMode() {
        setState {
            copy(
                isInSelectionMode = !isInSelectionMode,
                selectedCitySet = if (isInSelectionMode) emptySet() else selectedCitySet
            )
        }
    }

    private fun toggleCitySelection(city: String) {
        setState {
            val newSet = selectedCitySet.toMutableSet()
            if (city in newSet) newSet.remove(city) else newSet.add(city)
            copy(selectedCitySet = newSet)
        }
    }

    private fun deleteSelectedCities() {
        viewModelScope.launch {
            val toDelete = uiState.value.selectedCitySet.toMutableSet()
            runCatching { dataStoreUseCase.deleteCity(toDelete) }
            setState { copy(isInSelectionMode = false, selectedCitySet = emptySet()) }
        }
    }
}

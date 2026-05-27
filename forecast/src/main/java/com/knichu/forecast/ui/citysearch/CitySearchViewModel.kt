package com.knichu.forecast.ui.citysearch

import androidx.lifecycle.viewModelScope
import com.knichu.common.base.BaseMviViewModel
import com.knichu.domain.useCase.DataStoreUseCase
import com.knichu.domain.useCase.SearchCityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CitySearchViewModel @Inject constructor(
    private val searchCityUseCase: SearchCityUseCase,
    private val dataStoreUseCase: DataStoreUseCase
) : BaseMviViewModel<CitySearchUiIntent, CitySearchUiState, CitySearchUiEffect>() {

    override fun initialState() = CitySearchUiState()

    override fun handleIntent(intent: CitySearchUiIntent) {
        when (intent) {
            is CitySearchUiIntent.Search      -> search(intent.query)
            is CitySearchUiIntent.AddCity     -> addCity(intent.cityName)
            is CitySearchUiIntent.NavigateBack -> sendEffect(CitySearchUiEffect.NavigateBack)
        }
    }

    private fun search(query: String) {
        setState { copy(searchQuery = query) }
        viewModelScope.launch {
            runCatching { searchCityUseCase.getFilteredCityList(query) }
                .onSuccess { result -> setState { copy(searchedCityList = result.item ?: emptyList()) } }
                .onFailure { setState { copy(searchedCityList = emptyList()) } }
        }
    }

    private fun addCity(cityName: String) {
        viewModelScope.launch {
            runCatching { dataStoreUseCase.storeCity(cityName) }
                .onSuccess { sendEffect(CitySearchUiEffect.NavigateBack) }
                .onFailure { sendEffect(CitySearchUiEffect.ShowToast("이미 도시가 추가되어 있습니다")) }
        }
    }
}

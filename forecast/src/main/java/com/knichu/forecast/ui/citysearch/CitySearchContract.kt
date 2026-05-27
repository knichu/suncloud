package com.knichu.forecast.ui.citysearch

import com.knichu.domain.vo.CityLocationItemVO

data class CitySearchUiState(
    val searchQuery: String = "",
    val searchedCityList: List<CityLocationItemVO> = emptyList()
)

sealed class CitySearchUiIntent {
    data class Search(val query: String) : CitySearchUiIntent()
    data class AddCity(val cityName: String) : CitySearchUiIntent()
    object NavigateBack : CitySearchUiIntent()
}

sealed class CitySearchUiEffect {
    object NavigateBack : CitySearchUiEffect()
    data class ShowToast(val message: String) : CitySearchUiEffect()
}

package com.knichu.domain.useCase

import com.knichu.domain.vo.CityLocationVO

interface SearchCityUseCase {
    suspend fun getFilteredCityList(city: String): CityLocationVO
}

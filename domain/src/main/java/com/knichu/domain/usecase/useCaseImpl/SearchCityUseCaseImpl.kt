package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.useCase.SearchCityUseCase
import com.knichu.domain.util.FilteredCityParser
import com.knichu.domain.vo.CityLocationVO
import javax.inject.Inject

class SearchCityUseCaseImpl @Inject constructor(
    private val cityLocationRepository: CityLocationRepository
) : SearchCityUseCase {

    @Volatile private var cachedCityLocationVO: CityLocationVO? = null

    private suspend fun getCityList(): CityLocationVO =
        cachedCityLocationVO ?: cityLocationRepository.getAllCityLocation().also { cachedCityLocationVO = it }

    override suspend fun getFilteredCityList(city: String): CityLocationVO {
        return FilteredCityParser.filterCityString(city, getCityList())
    }
}

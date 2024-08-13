package com.knichu.domain.useCase.useCaseImpl

import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.useCase.SearchCityUseCase
import com.knichu.domain.util.FilteredCityParser.filterCityString
import com.knichu.domain.vo.CityLocationVO
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class SearchCityUseCaseImpl @Inject constructor(
    private val cityLocationRepository: CityLocationRepository
): SearchCityUseCase {
    private val allCityList = cityLocationRepository.getAllCityLocation()

    override fun getFilteredCityList(city: String): Single<CityLocationVO> {
        return allCityList
            .flatMap { cityLocationVO ->
                filterCityString(city, cityLocationVO)
            }
    }
}
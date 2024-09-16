package com.knichu.domain.useCase

import com.knichu.domain.vo.CityLocationVO
import io.reactivex.rxjava3.core.Single

interface SearchCityUseCase {
    fun getFilteredCityList(city: String): Single<CityLocationVO>
}
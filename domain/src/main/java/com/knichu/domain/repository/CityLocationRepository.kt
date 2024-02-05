package com.knichu.domain.repository

import com.knichu.domain.vo.CityLocationVO
import io.reactivex.rxjava3.core.Single

interface CityLocationRepository {
    fun getAllCityLocation(): Single<CityLocationVO>
}
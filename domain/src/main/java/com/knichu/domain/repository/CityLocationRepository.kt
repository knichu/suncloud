package com.knichu.domain.repository

import com.knichu.domain.vo.CityLocationVO
interface CityLocationRepository {
    suspend fun getAllCityLocation(): CityLocationVO
}
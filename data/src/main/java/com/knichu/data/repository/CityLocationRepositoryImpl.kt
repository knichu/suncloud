package com.knichu.data.repository

import com.knichu.data.datasource.CityLocationDataSource
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.vo.CityLocationVO
import javax.inject.Inject

class CityLocationRepositoryImpl @Inject constructor(
    private val cityLocationDataSource: CityLocationDataSource
) : CityLocationRepository {

    override suspend fun getAllCityLocation(): CityLocationVO {
        return try {
            cityLocationDataSource.getAllCityLocation().toDomain()
        } catch (e: Exception) {
            CityLocationVO(item = emptyList())
        }
    }
}

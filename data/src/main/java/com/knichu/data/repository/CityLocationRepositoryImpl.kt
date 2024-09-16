package com.knichu.data.repository

import com.knichu.data.datasource.CityLocationDataSource
import com.knichu.domain.repository.CityLocationRepository
import com.knichu.domain.vo.CityLocationVO
import com.knichu.domain.vo.LiveWeatherVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CityLocationRepositoryImpl @Inject constructor(
    private val cityLocationDataSource: CityLocationDataSource
): CityLocationRepository {
    override fun getAllCityLocation(): Single<CityLocationVO> {
        return cityLocationDataSource.getAllCityLocation()
            .map { responseDTO -> responseDTO.toDomain() }
            .onErrorReturn { CityLocationVO(item = emptyList()) }
    }
}
package com.knichu.data.datasource

import com.knichu.data.dto.response.CityLocationResponseDTO
import com.knichu.data.service.CityLocationService
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class CityLocationDataSource @Inject constructor(
    private val cityLocationService: CityLocationService
) : BaseNetworkDataSource() {
    fun getAllCityLocation(): Single<CityLocationResponseDTO> {
        return cityLocationService.getAllCityLocation()
            .map { checkResponse(it) }
            .subscribeOn(Schedulers.io())
    }
}
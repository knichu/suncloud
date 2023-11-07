package com.knichu.data.datasource

import com.knichu.data.dto.request.AirPollutionRequestDTO
import com.knichu.data.dto.response.AirPollutionResponseDTO
import com.knichu.data.service.AirPollutionService
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class AirPollutionDataSource @Inject constructor(
    private val airPollutionService: AirPollutionService
) : BaseNetworkDataSource() {
    fun getAirPollution(airPollutionRequest: AirPollutionRequestDTO): Single<AirPollutionResponseDTO> {
        return airPollutionService.getAirPollution(airPollutionRequest)
            .subscribeOn(Schedulers.io())
            .map { response -> checkResponse(response) }
    }
}
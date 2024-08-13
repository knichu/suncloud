package com.knichu.domain.util

import com.knichu.domain.vo.CityLocationVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

object FilteredCityParser {

    fun filterCityString(
        city: String,
        allCityList: CityLocationVO
    ): Single<CityLocationVO> {
        return Single.fromCallable {
            val filteredItems = allCityList.item?.filter {
                it.cityName?.contains(city, ignoreCase = true) == true
            } ?: emptyList()

            CityLocationVO(filteredItems)
        }
            .observeOn(Schedulers.computation())
    }
}
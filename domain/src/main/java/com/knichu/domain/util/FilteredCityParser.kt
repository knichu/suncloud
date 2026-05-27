package com.knichu.domain.util

import com.knichu.domain.vo.CityLocationVO

object FilteredCityParser {

    fun filterCityString(city: String, allCityList: CityLocationVO): CityLocationVO {
        val filteredItems = allCityList.item?.filter {
            it.cityName?.contains(city, ignoreCase = true) == true
        } ?: emptyList()
        return CityLocationVO(filteredItems)
    }
}

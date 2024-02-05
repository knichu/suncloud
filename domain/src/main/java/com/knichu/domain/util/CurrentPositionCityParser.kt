package com.knichu.domain.util

import com.knichu.domain.vo.CityLocationItemVO
import com.knichu.domain.vo.CityLocationVO
import kotlin.math.pow
import kotlin.math.sqrt

object CurrentPositionCityParser {

    fun getNearestCity(lon: Double, lat: Double, cityLocationVO: CityLocationVO): CityLocationItemVO? {
        return cityLocationVO.item?.minByOrNull { city ->
            city.longitude?.toDoubleOrNull()?.let { cityLon ->
                city.latitude?.toDoubleOrNull()?.let { cityLat ->
                    calculateDistance(lon, lat, cityLon, cityLat)
                }
            } ?: Double.MAX_VALUE
        }
    }

    private fun calculateDistance(lon1: Double, lat1: Double, lon2: Double, lat2: Double): Double {
        val lonDiff = lon2 - lon1
        val latDiff = lat2 - lat1
        return sqrt(lonDiff.pow(2) + latDiff.pow(2))
    }
}
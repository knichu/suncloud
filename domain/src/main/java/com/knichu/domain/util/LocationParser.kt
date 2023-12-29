package com.knichu.domain.util

import kotlin.math.*

class LocationParser {
    // X축 격자점 수 = 149
    // Y축 격자점 수 = 253

    data class Map(
        var Re: Double = 6371.00877,  // 지도반경
        var grid: Double = 5.0,  // 격자간격 (km)
        var slat1: Double = 30.0,  // 표준위도 1
        var slat2: Double = 60.0,  // 표준위도 2
        var olon: Double = 126.0,  // 기준점 경도
        var olat: Double = 38.0,  // 기준점 위도
        var xo: Double = 210.0 / grid,  // 기준점 X좌표
        var yo: Double = 675.0 / grid  // 기준점 Y좌표
    )

    fun getNxNy(lon: Double, lat: Double): Pair<Long, Long> {
        val map = Map()
        val (x, y) = mapConv(lon, lat, map)
        return Pair(x.toLong(), y.toLong())
    }

    private fun mapConv(lon: Double, lat: Double, map: Map): Pair<Double, Double> {
        val (x, y) = lamcProj(lon, lat, map)
        return Pair((x + 1.5), (y + 1.5))
    }

    private fun lamcProj(lon: Double, lat: Double, map: Map): Pair<Double, Double> {
        val PI = asin(1.0) * 2.0
        val DEGRAD = PI / 180.0

        val re = map.Re / map.grid
        val slat1 = map.slat1 * DEGRAD
        val slat2 = map.slat2 * DEGRAD
        val olon = map.olon * DEGRAD
        val olat = map.olat * DEGRAD

        var sn = tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5)
        sn = log(cos(slat1) / cos(slat2), 2.0) / log(sn, 2.0)
        var sf = tan(PI * 0.25 + slat1 * 0.5)
        sf = sf.pow(sn) * cos(slat1) / sn
        var ro = tan(PI * 0.25 + olat * 0.5)
        ro = re * sf / ro.pow(sn)

        var ra = tan(PI * 0.25 + lat * DEGRAD * 0.5)
        ra = re * sf / ra.pow(sn)
        var theta = lon * DEGRAD - olon
        if (theta > PI) {
            theta -= 2.0 * PI
        }
        if (theta < -PI) {
            theta += 2.0 * PI
        }
        theta *= sn
        val x = ((ra * sin(theta)) + map.xo)
        val y = ((ro - ra * cos(theta)) + map.yo)
        return Pair(x, y)
    }

}
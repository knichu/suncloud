package com.knichu.domain.util

import com.knichu.domain.vo.AirPollutionDataVO
import com.knichu.domain.vo.AirPollutionVO
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlin.math.roundToInt

object AirPollutionParser {

    fun getAirPollutionVO(airPollutionVO: Single<AirPollutionVO>): Single<AirPollutionDataVO> {
        return airPollutionVO.map { airPollution ->
            val pm2_5Quality = when {
                airPollution.pm2_5 == null -> "0"
                airPollution.pm2_5 < 15.0 -> "1"
                airPollution.pm2_5 >= 15.0 && airPollution.pm2_5 < 35.0 -> "2"
                airPollution.pm2_5 >= 35.0 && airPollution.pm2_5 < 75.0 -> "3"
                airPollution.pm2_5 >= 75.0 -> "4"
                else -> "0"
            }

            val pm10Quality = when {
                airPollution.pm10 == null -> "0"
                airPollution.pm10 < 30.0 -> "1"
                airPollution.pm10 >= 30.0 && airPollution.pm10 < 80.0 -> "2"
                airPollution.pm10 >= 80.0 && airPollution.pm10 < 150.0 -> "3"
                airPollution.pm10 >= 150.0 -> "4"
                else -> "0"
            }

            AirPollutionDataVO(
                pm2_5Density = (((airPollution.pm2_5?.times(10))?.roundToInt() ?: 10) / 10.0).toString(),
                pm10Density = (((airPollution.pm10?.times(10))?.roundToInt() ?: 15) / 10.0).toString(),
                pm2_5Quality = pm2_5Quality,
                pm10Quality = pm10Quality
            )
        }
            .observeOn(Schedulers.computation())
    }
}
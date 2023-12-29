package com.knichu.suncloud.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AirPollutionQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class CityLocationQualifier
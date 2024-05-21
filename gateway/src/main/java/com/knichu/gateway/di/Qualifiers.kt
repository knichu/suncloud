package com.knichu.gateway.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AirPollutionQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class OpenWeatherQualifier
package com.knichu.suncloud.di

import android.content.Context
import com.knichu.suncloud.util.AssetJsonConverter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL_AIR_POLLUTION = "https://api.openweathermap.org/data/2.5/"
    private const val BASE_URL_WEATHER = "https://apis.data.go.kr/1360000/"
    private const val BASE_URL_CITY_LOCATION = "http://example.com/"

    @Provides
    @Singleton
    fun provideRequestHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(interceptor: HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    @AirPollutionQualifier
    fun provideAirPollutionRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_AIR_POLLUTION)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @WeatherQualifier
    fun provideWeatherRetrofit(okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_WEATHER)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @CityLocationQualifier
    fun provideCityLocationRetrofit(
        okHttpClient: OkHttpClient,
        @ApplicationContext context: Context
    ) : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_CITY_LOCATION)
            .client(okHttpClient)
            .addConverterFactory(AssetJsonConverter(context))
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
    }
}
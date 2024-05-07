package com.knichu.gateway.di

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder
import androidx.datastore.rxjava3.RxDataStore
import com.knichu.data.datastore.WeatherDataStoreImpl
import com.knichu.domain.datastore.WeatherDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    private const val DATASTORE_NAME = "SUNCLOUD_DATSTORE"

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): WeatherDataStore {
        val dataStore: RxDataStore<Preferences> = RxPreferenceDataStoreBuilder(
            context = context, name = DATASTORE_NAME
        ).build()
        return WeatherDataStoreImpl(dataStore)
    }
}
package com.knichu.suncloud.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
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

    private const val DATASTORE_NAME = "SUNCLOUD_DATASTORE"

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): WeatherDataStore {
        val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(DATASTORE_NAME) }
        )
        return WeatherDataStoreImpl(dataStore)
    }
}

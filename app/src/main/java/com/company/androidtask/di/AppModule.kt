package com.company.androidtask.di

import android.content.Context
import android.content.SharedPreferences
import com.company.androidtask.data.manager.CacheManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePreferences(
        @ApplicationContext context: Context,
    ): SharedPreferences {
        return context.getSharedPreferences(
            "AppSharePrefs",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    @Singleton
    fun provideCacheManager(
        pref: SharedPreferences
    ): CacheManager {
        return CacheManager(pref)
    }

}
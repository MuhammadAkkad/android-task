package com.company.androidtask.di

import android.content.Context
import androidx.room.Room
import com.company.androidtask.data.common.Constants.DATABASE_NAME
import com.company.androidtask.data.db.AppDatabase
import com.company.androidtask.data.db.TasksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {

    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideTasksDao(database: AppDatabase): TasksDao {
        return database.tasksDao()
    }
}
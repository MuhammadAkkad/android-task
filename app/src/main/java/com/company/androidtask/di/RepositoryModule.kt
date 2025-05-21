package com.company.androidtask.di

import com.company.androidtask.data.repository.LoginRepositoryImpl
import com.company.androidtask.data.repository.TasksRepositoryImpl
import com.company.androidtask.domain.LoginRepository
import com.company.androidtask.domain.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindLoginRepository(
        loginRepositoryImpl: LoginRepositoryImpl
    ): LoginRepository

    @Binds
    @Singleton
    abstract fun bindTasksRepository(
        tasksRepositoryImpl: TasksRepositoryImpl
    ): TasksRepository
}
package com.yourapp.econ.app.di

import com.yourapp.econ.data.repository.AuthRepositoryImpl
import com.yourapp.econ.data.repository.RatesRepositoryImpl
import com.yourapp.econ.domain.repository.AuthRepository
import com.yourapp.econ.domain.repository.RatesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun ratesRepo(impl: RatesRepositoryImpl): RatesRepository = impl

    @Provides
    @Singleton
    fun authRepo(impl: AuthRepositoryImpl): AuthRepository = impl
}

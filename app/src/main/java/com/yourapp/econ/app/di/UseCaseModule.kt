package com.yourapp.econ.app.di

import com.yourapp.econ.domain.repository.AuthRepository
import com.yourapp.econ.domain.repository.RatesRepository
import com.yourapp.econ.domain.usecase.GetLatestRatesUseCase
import com.yourapp.econ.domain.usecase.GetSessionUseCase
import com.yourapp.econ.domain.usecase.GetTimeSeriesUseCase
import com.yourapp.econ.domain.usecase.LoginUseCase
import com.yourapp.econ.domain.usecase.LogoutUseCase
import com.yourapp.econ.domain.usecase.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    fun latest(repo: RatesRepository) = GetLatestRatesUseCase(repo)

    @Provides
    fun series(repo: RatesRepository) = GetTimeSeriesUseCase(repo)

    @Provides
    fun register(repo: AuthRepository) = RegisterUseCase(repo)

    @Provides
    fun login(repo: AuthRepository) = LoginUseCase(repo)

    @Provides
    fun logout(repo: AuthRepository) = LogoutUseCase(repo)

    @Provides
    fun session(repo: AuthRepository) = GetSessionUseCase(repo)
}

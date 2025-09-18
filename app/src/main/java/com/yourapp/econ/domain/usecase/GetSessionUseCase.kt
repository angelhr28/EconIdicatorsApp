package com.yourapp.econ.domain.usecase

import com.yourapp.econ.domain.repository.AuthRepository

class GetSessionUseCase(private val repo: AuthRepository) {
    fun invoke() = repo.observeSession()
}
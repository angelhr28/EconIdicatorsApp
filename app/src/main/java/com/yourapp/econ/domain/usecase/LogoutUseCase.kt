package com.yourapp.econ.domain.usecase

import com.yourapp.econ.domain.repository.AuthRepository

class LogoutUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke() = repo.logout()
}

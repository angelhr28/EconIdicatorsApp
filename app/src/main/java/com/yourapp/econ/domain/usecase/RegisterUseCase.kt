package com.yourapp.econ.domain.usecase

import com.yourapp.econ.domain.repository.AuthRepository

class RegisterUseCase(private val repo: AuthRepository) {
    suspend operator fun invoke(email: String, name: String, lastName: String, password: String) =
        repo.register(email, name, lastName, password)
}

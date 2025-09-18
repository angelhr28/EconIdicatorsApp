package com.yourapp.econ.domain.usecase

import com.yourapp.econ.domain.repository.RatesRepository

class GetLatestRatesUseCase(private val repo: RatesRepository) {
    suspend operator fun invoke(base: String, symbols: List<String>) = repo.getLatest(base, symbols)
}

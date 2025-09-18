package com.yourapp.econ.domain.usecase

import com.yourapp.econ.domain.repository.RatesRepository

class GetTimeSeriesUseCase(private val repo: RatesRepository) {
    suspend operator fun invoke(
        base: String,
        symbols: List<String>,
        year: Int,
    ) = repo.getTimeSeriesYear(base, symbols, year)
}

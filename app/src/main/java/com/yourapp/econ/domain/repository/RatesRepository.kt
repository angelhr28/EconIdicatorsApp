package com.yourapp.econ.domain.repository

import com.yourapp.econ.domain.entity.IndicatorPoint
import com.yourapp.econ.domain.entity.IndicatorValue

interface RatesRepository {
    suspend fun getLatest(base: String, symbols: List<String>): List<IndicatorValue>
    suspend fun getTimeSeriesYear(
        base: String,
        symbols: List<String>,
        year: Int
    ): List<IndicatorPoint>
}

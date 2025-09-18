package com.yourapp.econ.feature.indicators.state

import com.yourapp.econ.core.mvi.State
import com.yourapp.econ.domain.entity.IndicatorPoint
import com.yourapp.econ.domain.entity.IndicatorValue
import java.time.LocalDate

data class IndState(
    val isLoading: Boolean,
    val base: String,
    val symbols: List<String>,
    val selectedYear: Int,
    val todayValues: List<IndicatorValue>,
    val series: List<IndicatorPoint>,
    val error: String? = null
) : State {
    companion object {
        fun initial() = IndState(
            isLoading = false,
            base = "USD",
            symbols = listOf("USD", "EUR", "CLP"),
            selectedYear = LocalDate.now().year,
            todayValues = emptyList(),
            series = emptyList(),
            error = null
        )
    }
}

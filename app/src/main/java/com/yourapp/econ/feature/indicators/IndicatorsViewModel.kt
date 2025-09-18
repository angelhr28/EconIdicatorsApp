package com.yourapp.econ.feature.indicators

import androidx.lifecycle.viewModelScope
import com.yourapp.econ.core.mvi.BaseViewModel
import com.yourapp.econ.core.mvi.Reducer
import com.yourapp.econ.domain.entity.IndicatorValue
import com.yourapp.econ.domain.usecase.GetLatestRatesUseCase
import com.yourapp.econ.domain.usecase.GetTimeSeriesUseCase
import com.yourapp.econ.domain.usecase.LogoutUseCase
import com.yourapp.econ.feature.indicators.intent.IndIntent
import com.yourapp.econ.feature.indicators.state.IndState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class IndicatorsViewModel @Inject constructor(
    private val getLatest: GetLatestRatesUseCase,
    private val getSeries: GetTimeSeriesUseCase,
    private val logout: LogoutUseCase
) : BaseViewModel<IndState, IndIntent>() {

    private val reducer = IndicatorsReducer(IndState.initial())

    override val state: StateFlow<IndState> get() = reducer.state
    override fun updateState(newState: (IndState) -> IndState) {
        reducer.setState(newState(reducer.state.value))
    }

    private fun send(i: IndIntent) = reducer.sendIntent(i)

    inner class IndicatorsReducer(initial: IndState) : Reducer<IndState, IndIntent>(initial) {
        override fun reduce(oldState: IndState, intent: IndIntent) {
            when (intent) {
                is IndIntent.Load -> loadForYear(LocalDate.now().year)
                is IndIntent.ChangeYear -> loadForYear(intent.year)
                IndIntent.Refresh -> loadForYear(state.value.selectedYear)
                IndIntent.ErrorShown -> updateState { it.copy(error = null) }
                IndIntent.Logout -> logoutSession()
            }
        }
    }

    private fun loadForYear(year: Int) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, selectedYear = year, error = null) }
            try {
                val today = LocalDate.now()
                val series = getSeries(
                    base = "USD",
                    symbols = state.value.symbols,
                    year = year
                )

                val headerValues: List<IndicatorValue>

                if (year == today.year) {
                    headerValues = getLatest(base = "USD", symbols = state.value.symbols)
                } else {
                    val lastPoint = series.maxByOrNull { it.date }
                    headerValues = lastPoint?.let { lp ->
                        mapToValues(
                            map = lp.values,
                            symbols = state.value.symbols
                        )
                    } ?: emptyList()
                }

                updateState {
                    it.copy(
                        isLoading = false,
                        todayValues = headerValues,
                        series = series,
                        selectedYear = year,
                    )
                }
            } catch (e: Throwable) {
                updateState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error",
                        series = emptyList(),
                        todayValues = emptyList()
                    )
                }
            }
        }
    }

    private fun mapToValues(
        map: Map<String, Double>,
        symbols: List<String>
    ): List<IndicatorValue> =
        symbols.mapNotNull { code -> map[code]?.let { v -> IndicatorValue(code, v) } }

    private fun logoutSession() {
        viewModelScope.launch { logout() }
    }

    fun onLoad() = send(IndIntent.Load)
    fun onChangeYear(year: Int) = send(IndIntent.ChangeYear(year))
    fun onRefresh() = send(IndIntent.Refresh)
    fun onLogout() = send(IndIntent.Logout)
}

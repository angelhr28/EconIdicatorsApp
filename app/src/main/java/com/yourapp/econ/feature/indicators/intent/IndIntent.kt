package com.yourapp.econ.feature.indicators.intent

import com.yourapp.econ.core.mvi.Intent

sealed interface IndIntent : Intent {
    data object Load : IndIntent
    data class ChangeYear(val year: Int) : IndIntent
    data object Refresh : IndIntent
    data object ErrorShown : IndIntent
    data object Logout : IndIntent
}
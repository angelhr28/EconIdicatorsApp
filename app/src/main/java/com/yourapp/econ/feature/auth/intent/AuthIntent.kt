package com.yourapp.econ.feature.auth.intent

import com.yourapp.econ.core.mvi.Intent

sealed interface AuthIntent : Intent {
    data class SubmitLogin(val email: String, val password: String) : AuthIntent
    data class SubmitRegister(
        val email: String,
        val name: String,
        val lastName: String,
        val password: String
    ) : AuthIntent

    data object ErrorShown : AuthIntent
}
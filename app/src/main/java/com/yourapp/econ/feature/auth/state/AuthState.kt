package com.yourapp.econ.feature.auth.state

import com.yourapp.econ.core.mvi.State
import com.yourapp.econ.domain.entity.Session

data class AuthState(
    val isLoading: Boolean,
    val session: Session?,
    val error: String?
) : State {
    companion object {
        fun initial() = AuthState(
            isLoading = false,
            session = null,
            error = null
        )
    }
}
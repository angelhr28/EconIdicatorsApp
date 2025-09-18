package com.yourapp.econ.feature.auth

import androidx.lifecycle.viewModelScope
import com.yourapp.econ.core.common.AppError
import com.yourapp.econ.core.mvi.BaseViewModel
import com.yourapp.econ.core.mvi.Reducer
import com.yourapp.econ.domain.usecase.GetSessionUseCase
import com.yourapp.econ.domain.usecase.LoginUseCase
import com.yourapp.econ.domain.usecase.RegisterUseCase
import com.yourapp.econ.feature.auth.intent.AuthIntent
import com.yourapp.econ.feature.auth.state.AuthState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val login: LoginUseCase,
    private val register: RegisterUseCase,
    private val session: GetSessionUseCase
) : BaseViewModel<AuthState, AuthIntent>() {

    private val reducer = AuthReducer(AuthState.initial())

    override val state: StateFlow<AuthState> get() = reducer.state

    override fun updateState(newState: (AuthState) -> AuthState) {
        reducer.setState(newState(reducer.state.value))
    }

    private fun sendIntent(intent: AuthIntent) = reducer.sendIntent(intent)

    init {
        viewModelScope.launch {
            session.invoke().collect { s ->
                updateState { it.copy(session = s) }
            }
        }
    }

    inner class AuthReducer(initialValue: AuthState) :
        Reducer<AuthState, AuthIntent>(initialValue) {
        override fun reduce(oldState: AuthState, intent: AuthIntent) {
            when (intent) {
                is AuthIntent.SubmitLogin -> launchCatching {
                    updateState { it.copy(isLoading = true, error = null) }
                    val s = login(intent.email, intent.password)
                    updateState { it.copy(isLoading = false, session = s) }
                }

                is AuthIntent.SubmitRegister -> launchCatching {
                    updateState { it.copy(isLoading = true, error = null) }
                    val s = register(intent.email, intent.name, intent.lastName, intent.password)
                    updateState { it.copy(isLoading = false, session = s) }
                }

                AuthIntent.ErrorShown -> updateState { it.copy(error = null) }
            }
        }
    }


    private inline fun launchCatching(crossinline block: suspend () -> Unit) {
        viewModelScope.launch {
            try {
                block()
            } catch (e: AppError) {
                updateState { it.copy(isLoading = false, error = e.message) }
            } catch (e: Throwable) {
                updateState { it.copy(isLoading = false, error = "Unexpected error") }
            }
        }
    }

    fun onSubmitLogin(email: String, pass: String) = sendIntent(AuthIntent.SubmitLogin(email, pass))
    fun onErrorShown() = sendIntent(AuthIntent.ErrorShown)
    fun onSubmitRegister(email: String, name: String, last: String, pass: String) =
        sendIntent(AuthIntent.SubmitRegister(email, name, last, pass))
}

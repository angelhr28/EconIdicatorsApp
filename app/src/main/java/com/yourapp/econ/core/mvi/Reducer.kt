package com.yourapp.econ.core.mvi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


abstract class Reducer<S : State, A : Intent>(initialValue: S) {

    private val _state: MutableStateFlow<S> = MutableStateFlow(initialValue)
    val state: StateFlow<S> get() = _state

    fun sendIntent(intent: A) = reduce(_state.value, intent)

    fun setState(newState: S) {
        _state.tryEmit(newState)
    }

    abstract fun reduce(oldState: S, intent: A)
}

interface State
interface Intent

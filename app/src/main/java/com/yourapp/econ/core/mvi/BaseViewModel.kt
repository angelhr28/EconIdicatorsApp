package com.yourapp.econ.core.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

abstract class BaseViewModel<S : State, in A : Intent> : ViewModel() {

    abstract val state: Flow<S>

    abstract fun updateState(newState: (S) -> S)
}

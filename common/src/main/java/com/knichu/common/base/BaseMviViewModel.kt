package com.knichu.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseMviViewModel<Intent, State, Effect> : ViewModel() {

    private val _uiState: MutableStateFlow<State> by lazy {
        MutableStateFlow(initialState())
    }
    val uiState: StateFlow<State> by lazy { _uiState.asStateFlow() }

    private val _uiEffect = Channel<Effect>(Channel.BUFFERED)
    val uiEffect: Flow<Effect> = _uiEffect.receiveAsFlow()

    abstract fun initialState(): State

    abstract fun handleIntent(intent: Intent)

    protected fun setState(reducer: State.() -> State) {
        _uiState.update { it.reducer() }
    }

    protected fun sendEffect(effect: Effect) {
        viewModelScope.launch {
            _uiEffect.send(effect)
        }
    }
}

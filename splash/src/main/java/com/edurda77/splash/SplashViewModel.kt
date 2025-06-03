package com.edurda77.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.model.LocalAuthResult
import com.edurda77.domain.usecase.AuthCheckUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SplashViewModel(
    private val authCheckUseCase: AuthCheckUseCase
): ViewModel() {
    private var _state = MutableStateFlow(SplashScreenState.LOADING)
    val state = _state.onStart {
        getSavedLogin()
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            SplashScreenState.LOADING
        )

    /* init {
         getSavedLogin()
     }*/

    private fun getSavedLogin() {
        viewModelScope.launch {
            delay(2000)
            authCheckUseCase.invoke().collect { collector ->
                when (collector) {
                    LocalAuthResult.LocalNotSession -> {
                        _state.value = SplashScreenState.NOT_AUTHORIZED
                    }
                    is LocalAuthResult.LocalSession -> {
                        _state.value = SplashScreenState.AUTHORIZED
                    }
                }
            }
        }
    }
}
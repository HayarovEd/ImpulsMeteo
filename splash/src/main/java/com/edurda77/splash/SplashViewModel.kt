package com.edurda77.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.model.LocalAuthResult
import com.edurda77.domain.usecase.AuthCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authCheckUseCase: AuthCheckUseCase
): ViewModel() {
    private var _state = MutableStateFlow(SplashScreenState.LOADING)
    val state = _state.asStateFlow()

    init {
        getSavedLogin()
    }

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
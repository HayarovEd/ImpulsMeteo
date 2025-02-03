package com.edurda77.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.LoginUseCase
import com.edurda77.domain.usecase.ReadLocalAuthorizationUseCase
import com.edurda77.domain.usecase.SaveLocalAuthorizationUseCase
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.uikit.asUiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val readLocalAuthorizationUseCase: ReadLocalAuthorizationUseCase,
    private val saveLocalAuthorizationUseCase: SaveLocalAuthorizationUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            loadLocalAuthorization()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            LoginState()
        )

    private val _eventFlow = MutableSharedFlow<UiLoginEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    /* init {
         loadLocalAuthorization()
     }*/

    private fun loadLocalAuthorization() {
        viewModelScope.launch {
            readLocalAuthorizationUseCase.invoke().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        val errorMessage = collector.error.asUiText()
                        _eventFlow.emit(
                            UiLoginEvents.SnackbarEvent(errorMessage)
                        )
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            email = collector.data.email,
                            password = collector.data.password
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            LoginEvent.OnLogin -> {
                _state.value.copy(
                    isLoading = true
                )
                    .updateState()
                viewModelScope.launch {
                    saveLocalAuthorizationUseCase.invoke(
                        email = state.value.email,
                        password = state.value.password
                    )
                }
                viewModelScope.launch {
                    val result = loginUseCase.invoke(
                        email = _state.value.email,
                        password = _state.value.password
                    )
                    when (result) {
                        is ResultWork.Error -> {
                            val errorMessage = result.error.asUiText()
                            _eventFlow.emit(
                                UiLoginEvents.SnackbarEvent(errorMessage)
                            )
                        }

                        is ResultWork.Success -> {
                            _eventFlow.emit(
                                UiLoginEvents.NavigateEvent
                            )
                        }
                    }
                    _state.value.copy(
                        isLoading = false
                    )
                        .updateState()
                }
            }

            is LoginEvent.SetEmail -> {
                _state.value.copy(
                    email = event.email
                )
                    .updateState()
            }

            is LoginEvent.SetPassword -> {
                _state.value.copy(
                    password = event.password
                )
                    .updateState()
            }
        }
    }


    private fun LoginState.updateState() {
        _state.update {
            this
        }
    }


}
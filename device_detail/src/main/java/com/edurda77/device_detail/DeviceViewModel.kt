package com.edurda77.device_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edurda77.domain.model.NavigationRoute
import com.edurda77.domain.usecase.DeviceByIdUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.uikit.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceByIdUseCase: DeviceByIdUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private var _state = MutableStateFlow(DeviceState())
    val state = _state.asStateFlow()


    init {
        loadLocalData()
    }

    fun onEvent(event: DeviceEvent) {
        when (event) {
            is DeviceEvent.OnSetFromDate -> {
                _state.value.copy(
                    fromDate = event.dateTime
                )
                    .updateState()
            }

            is DeviceEvent.OnSetToDate -> {
                _state.value.copy(
                    toDate = event.dateTime
                )
                    .updateState()
            }

            is DeviceEvent.GetHistory -> {
                ////////
            }
        }
    }

    private fun loadLocalData() {
        viewModelScope.launch {
            localTokenUseCase.invoke().collect { collectedToken ->
                when (collectedToken) {
                    is ResultWork.Error -> {
                        _state.value.copy(
                            isLoading = false,
                            message = collectedToken.error.asUiText()
                        )
                            .updateState()
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            token = collectedToken.data.accessToken
                        )
                            .updateState()
                        delay(500)
                        loadLoggedUserData(collectedToken.data.accessToken)
                    }
                }
            }
        }
    }

    private suspend fun loadLoggedUserData(token: String) {
        val deviceId = savedStateHandle.toRoute<NavigationRoute.Device>().id

        _state.value.copy(
            deviceId = deviceId.toIntOrNull() ?: NEGATIVE_ID
        )
            .updateState()
        delay(500)
        when (val result = loggedUserUseCase.invoke(token)) {
            is ResultWork.Error -> {
                _state.value.copy(
                    isLoading = false,
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    loggedUser = result.data
                )
                    .updateState()
                loadDevice()
            }
        }
    }

    private suspend fun loadDevice() {
        val deviceId = state.value.deviceId
        if (deviceId != NEGATIVE_ID) {
            when (val result = deviceByIdUseCase.invoke(
                token = state.value.token,
                id = deviceId
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoading = false,
                        message = result.error.asUiText()
                    )
                        .updateState()
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        device = result.data
                    )
                        .updateState()
                }
            }
        }
    }


    private fun DeviceState.updateState() {
        _state.update {
            this
        }
    }
}
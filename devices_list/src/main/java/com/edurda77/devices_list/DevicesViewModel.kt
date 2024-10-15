package com.edurda77.devices_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.GrouppedDevicesUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
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
class DevicesViewModel @Inject constructor(
    private val groupedDevicesUseCase: GrouppedDevicesUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val logoffUseCase: LogOffUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(DevicesState())
    val state = _state.asStateFlow()


    init {
        loadLocalData()
    }

    fun onEvent(event: DevicesEvent) {
        when (event) {
            DevicesEvent.Logoff -> {
                viewModelScope.launch { logoffUseCase.invoke() }
            }

            is DevicesEvent.OnSearch -> {
                viewModelScope.launch {
                    _state.value.copy(
                        query = event.query
                    )
                        .updateState()
                    loadDevices(false)
                }
            }

            DevicesEvent.Refresh -> {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                viewModelScope.launch {
                    delay(1000)
                    loadDevices(true)
                }
            }

            is DevicesEvent.SelectGroup -> {
                viewModelScope.launch {
                    _state.value.copy(
                        numberSelectedGroup = event.index
                    )
                        .updateState()
                    //loadDevices(false)
                }
            }

            DevicesEvent.ShowSearchField -> {
                _state.value.copy(
                    isShowSearch = !state.value.isShowSearch
                )
                    .updateState()
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
                loadDevices(true)
            }
        }
    }

    private suspend fun loadDevices(isRefresh: Boolean) {
        when (val result = groupedDevicesUseCase.invoke(
            token = state.value.token,
            query = state.value.query,
            isRefresh = isRefresh
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
                    devices = result.data
                )
                    .updateState()
            }
        }
    }


    private fun DevicesState.updateState() {
        _state.update {
            this
        }
    }
}
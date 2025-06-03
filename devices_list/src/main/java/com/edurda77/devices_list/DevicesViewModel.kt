package com.edurda77.devices_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.AddDeviceUseCase
import com.edurda77.domain.usecase.AddFavoriteUseCase
import com.edurda77.domain.usecase.CloseWebsocketUseCase
import com.edurda77.domain.usecase.DeleteDeviceUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.GroupedDevicesUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.RemoveFavoriteUseCase
import com.edurda77.domain.usecase.WebSocketUseCase
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.updateDevices
import com.edurda77.resources.uikit.asUiText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DevicesViewModel(
    private val groupedDevicesUseCase: GroupedDevicesUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val logoffUseCase: LogOffUseCase,
    private val addDeviceUseCase: AddDeviceUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val closeWebsocketUseCase: CloseWebsocketUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(DevicesState())
    val state = _state
        .onStart {
            loadLocalData()
            loadUpdateData()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DevicesState()
        )


    /*init {
        loadLocalData()
        loadUpdateData()
    }*/

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
                    loadDevices(
                        isRefresh = false,
                        query = event.query)
                }
            }

            DevicesEvent.Refresh -> {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                viewModelScope.launch {
                    delay(1000)
                    loadDevices(
                        isRefresh = true,
                        query = state.value.query)
                }
            }

            is DevicesEvent.SelectGroup -> {
                viewModelScope.launch {
                    _state.value.copy(
                        numberSelectedGroup = event.index
                    )
                        .updateState()
                }
            }

            DevicesEvent.ShowSearchField -> {
                _state.value.copy(
                    isShowSearch = !state.value.isShowSearch
                )
                    .updateState()
            }

            is DevicesEvent.OnInsertDevice -> {
                viewModelScope.launch {
                    insertDevice(
                        name = event.name,
                        key = event.key,
                        frequency = event.frequency,
                        groups = event.groups.map { it.id }
                    )
                }
            }

            is DevicesEvent.UpdateSelectedGroups -> {
                val updatedGroups = state.value.selectedGroups.toMutableList()
                if (state.value.selectedGroups.contains(event.groupDevices)) {
                    updatedGroups.remove(event.groupDevices)
                } else {
                    updatedGroups.add(event.groupDevices)
                }
                _state.value.copy(
                    selectedGroups = updatedGroups
                )
                    .updateState()
            }

            DevicesEvent.ClearSelectedGroups -> {
                _state.value.copy(
                    selectedGroups = emptyList()
                )
                    .updateState()
            }

            DevicesEvent.OnCloseWebSocket -> {
                viewModelScope.launch {
                    closeWebsocketUseCase.invoke()
                }
            }

            is DevicesEvent.WorkWithFavorite -> {
                viewModelScope.launch {
                    if (event.device.isFavorite) {
                        removeFavoriteUseCase.invoke(
                            deviceId = event.device.id,
                        )
                    } else {
                        addFavoriteUseCase.invoke(
                            deviceId = event.device.id,
                        )
                    }
                }
            }

            is DevicesEvent.OnDeleteDevice -> {
                viewModelScope.launch {
                    deleteDeviceUseCase.invoke(
                        token = state.value.token,
                        isFavorite = event.device.isFavorite,
                        id = event.device.id
                    )
                    _state.value.copy(
                        isLoading = true,
                    )
                        .updateState()
                    loadDevices(
                        isRefresh = true,
                        query = state.value.query
                    )
                }
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

    private fun loadUpdateData() {
        viewModelScope.launch {
            delay(5000)
            webSocketUseCase.invoke(
                token = state.value.token,
                ids = state.value.devices.values.flatten().map { it.id }.toSet().toList()
            ).collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        _state.value.copy(
                            message = collector.error.asUiText()
                        )
                            .updateState()
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            devices = updateDevices(
                                devices = state.value.devices,
                                newDevice = collector.data
                            )
                        )
                            .updateState()
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
                viewModelScope.launch {
                    if (state.value.loggedUser?.permissions?.contains(DIRECTORY_LIST) == true) {
                        loadGroups()
                    }
                }
                loadDevices(
                    isRefresh = true,
                    query = state.value.query
                )
            }
        }
    }

    private suspend fun loadDevices(
        isRefresh: Boolean,
        query: String
    ) {
        when (val result = groupedDevicesUseCase.invoke(
            token = state.value.token,
            query = query,
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

    private suspend fun insertDevice(
        name: String,
        key: String,
        frequency: String,
        groups: List<Int>
    ) {
        when (val result = addDeviceUseCase.invoke(
            name = name,
            key = key,
            update = frequency,
            groups = groups,
            token = state.value.token
        )) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadDevices(
                    isRefresh = true,
                    query = state.value.query
                )
            }
        }
    }

    private suspend fun loadGroups() {
        when (val result = devicesGroupsUseCase.invoke(state.value.token)) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    groups = result.data
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

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            closeWebsocketUseCase.invoke()
        }
    }
}
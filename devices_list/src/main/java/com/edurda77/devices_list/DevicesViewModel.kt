package com.edurda77.devices_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.AddDeviceUseCase
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
    private val logoffUseCase: LogOffUseCase,
    private val addDeviceUseCase: AddDeviceUseCase,
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
                loadDevices(true)
            }
        }
    }


    private fun DevicesState.updateState() {
        _state.update {
            this
        }
    }
}
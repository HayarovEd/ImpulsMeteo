package com.edurda77.directories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.AddDevicesGroupUseCase
import com.edurda77.domain.usecase.AddUnitUseCase
import com.edurda77.domain.usecase.DeleteDevicesGroupUseCase
import com.edurda77.domain.usecase.DeleteUnitUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateDevicesGroupUseCase
import com.edurda77.domain.usecase.UpdateUnitUseCase
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.uikit.asUiText
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DirectoriesViewModel(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val logoffUseCase: LogOffUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val unitsUseCase: UnitsUseCase,
    private val addDevicesGroupUseCase: AddDevicesGroupUseCase,
    private val addUnitUseCase: AddUnitUseCase,
    private val deleteDevicesGroupUseCase: DeleteDevicesGroupUseCase,
    private val deleteUnitUseCase: DeleteUnitUseCase,
    private val updateDevicesGroupUseCase: UpdateDevicesGroupUseCase,
    private val updateUnitUseCase: UpdateUnitUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(DirectoriesState())
    val state = _state
        .onStart {
            loadUserData()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DirectoriesState()
        )

    private val _eventFlow = Channel<UiDirectoriesEvents>()
    val eventFlow = _eventFlow.receiveAsFlow()

    fun onEvent(event: DirectoriesEvent) {
        when (event) {
            DirectoriesEvent.Logoff -> {
                viewModelScope.launch {
                    logoffUseCase.invoke()
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                }
            }

            DirectoriesEvent.Refresh -> {
                loadUserData()
            }

            is DirectoriesEvent.SwitchDirectoriesType -> {
                _state.value.copy(
                    directoriesType = event.directoriesType,
                )
                    .updateState()
            }

            is DirectoriesEvent.AddDevicesGroup -> {
                viewModelScope.launch {
                    insertDevicesGroup(
                        name = event.name,
                    )
                }
            }

            is DirectoriesEvent.AddUnit -> {
                viewModelScope.launch {
                    insertUnit(
                        name = event.name,
                        short = event.short
                    )
                }
            }

            is DirectoriesEvent.DeleteDevicesGroup -> {
                viewModelScope.launch {
                    deleteDevicesGroup(
                        id = event.id
                    )
                }
            }

            is DirectoriesEvent.DeleteUnit -> {
                viewModelScope.launch {
                    deleteUnit(
                        id = event.id
                    )
                }
            }

            is DirectoriesEvent.UpdateDevicesGroup -> {
                viewModelScope.launch {
                    updateDevicesGroup(
                        id = event.id,
                        name = event.name
                    )
                }
            }

            is DirectoriesEvent.UpdateUnit -> {
                viewModelScope.launch {
                    updateUnit(
                        id = event.id,
                        name = event.name,
                        short = event.short
                    )
                }
            }

            is DirectoriesEvent.OnSearch -> {
                _state.value.copy(
                    query = event.query,
                )
                    .updateState()
            }
        }
    }

    private fun loadUserData() {
        _state.value.copy(
            isLoading = true,
        )
            .updateState()
        viewModelScope.launch {
            when (val result = loggedUserUseCase.invoke()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        authUser = result.data,
                    )
                        .updateState()
                    loadGroupsAndUnits()
                }
            }
        }
    }

    private fun loadGroupsAndUnits() {
        _state.value.copy(
            isLoading = true
        )
            .updateState()
        viewModelScope.launch {
            val resultGroupsDiff = async { devicesGroupsUseCase.invoke() }
            val resultUnitsDiff = async { unitsUseCase.invoke() }
            when (val result = resultGroupsDiff.await()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        groups = result.data
                    )
                        .updateState()
                }
            }
            when (val result = resultUnitsDiff.await()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        units = result.data
                    )
                        .updateState()
                }
            }
            _state.value.copy(
                isLoading = false,
            )
                .updateState()
        }
    }

    private suspend fun insertDevicesGroup(name: String) {
        when (val result = addDevicesGroupUseCase.invoke(
            token = state.value.token,
            name = name,
        )) {
            is ResultWork.Error -> {
                if (result.error is DataError.TokenError) {
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                } else {
                    _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                }
            }

            is ResultWork.Success -> {
                //   loadGroups()
            }
        }
    }

    private suspend fun insertUnit(
        name: String,
        short: String
    ) {
        when (val result = addUnitUseCase.invoke(
            token = state.value.token,
            name = name,
            short = short
        )) {
            is ResultWork.Error -> {
                if (result.error is DataError.TokenError) {
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                } else {
                    _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                }
            }

            is ResultWork.Success -> {
                //  loadUnits()
            }
        }
    }

    private suspend fun deleteDevicesGroup(id: Int) {
        when (val result = deleteDevicesGroupUseCase.invoke(
            token = state.value.token,
            id = id
        )) {
            is ResultWork.Error -> {
                if (result.error is DataError.TokenError) {
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                } else {
                    _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                }
            }

            is ResultWork.Success -> {
                //   loadGroups()
            }
        }
    }

    private suspend fun deleteUnit(id: Int) {
        when (val result = deleteUnitUseCase.invoke(
            token = state.value.token,
            id = id
        )) {
            is ResultWork.Error -> {
                if (result.error is DataError.TokenError) {
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                } else {
                    _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                }
            }

            is ResultWork.Success -> {
                //loadUnits()
            }
        }
    }

    private suspend fun updateDevicesGroup(id: Int, name: String) {
        when (val result = updateDevicesGroupUseCase.invoke(
            token = state.value.token,
            id = id,
            name = name
        )) {
            is ResultWork.Error -> {
                if (result.error is DataError.TokenError) {
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                } else {
                    _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                }
            }

            is ResultWork.Success -> {
                //  loadGroups()
            }
        }
    }

    private suspend fun updateUnit(
        id: Int,
        name: String,
        short: String
    ) {
        when (val result = updateUnitUseCase.invoke(
            token = state.value.token,
            id = id,
            name = name,
            short = short
        )) {
            is ResultWork.Error -> {
                if (result.error is DataError.TokenError) {
                    _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                } else {
                    _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                }
            }

            is ResultWork.Success -> {
                //  loadUnits()
            }
        }
    }

    private fun DirectoriesState.updateState() {
        _state.update {
            this
        }
    }
}
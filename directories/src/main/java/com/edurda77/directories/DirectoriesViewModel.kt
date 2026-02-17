package com.edurda77.directories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.MeasurementUnit
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
                insertDevicesGroup(
                    name = event.name,
                )
            }

            is DirectoriesEvent.AddUnit -> {
                insertUnit(
                    name = event.name,
                    short = event.short
                )
            }

            is DirectoriesEvent.DeleteDevicesGroup -> {
                deleteDevicesGroup(
                    id = event.id
                )
            }

            is DirectoriesEvent.DeleteUnit -> {
                deleteUnit(
                    id = event.id
                )
            }

            is DirectoriesEvent.UpdateDevicesGroup -> {
                updateDevicesGroup(
                    event.groupDevice
                )
            }

            is DirectoriesEvent.UpdateUnit -> {
                updateUnit(
                    event.measurementUnit
                )
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

    private fun insertDevicesGroup(name: String) {
        viewModelScope.launch {
            when (val result = addDevicesGroupUseCase.invoke(
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
                    _state.value.copy(
                        groups = state.value.groups + result.data
                    )
                        .updateState()
                }
            }
        }

    }

    private fun insertUnit(
        name: String,
        short: String
    ) {
        viewModelScope.launch {
            when (val result = addUnitUseCase.invoke(
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
                    _state.value.copy(
                        units = state.value.units + result.data
                    )
                        .updateState()
                }
            }
        }

    }

    private fun deleteDevicesGroup(id: String) {
        viewModelScope.launch {
            when (val result = deleteDevicesGroupUseCase.invoke(
                groupId = id
            )) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        groups = state.value.groups.filter { it.id != id }
                    )
                        .updateState()
                }
            }
        }

    }

    private fun deleteUnit(id: String) {
        viewModelScope.launch {
            when (val result = deleteUnitUseCase.invoke(
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
                    _state.value.copy(
                        units = state.value.units.filter { it.id != id }
                    )
                        .updateState()
                }
            }
        }

    }

    private fun updateDevicesGroup(groupDevice: GroupDevice) {
        viewModelScope.launch {
            when (val result = updateDevicesGroupUseCase.invoke(
                groupDevice
            )) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        groups = state.value.groups.map {
                            if (it.id == result.data.id) {
                                result.data
                            } else it
                        }
                    )
                        .updateState()
                }
            }
        }

    }

    private fun updateUnit(
        measurementUnit: MeasurementUnit,
    ) {
        viewModelScope.launch {
            when (val result = updateUnitUseCase.invoke(
                measurementUnit = measurementUnit
            )) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDirectoriesEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDirectoriesEvents.OnError(result.error.asUiText()))
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        units = state.value.units.map {
                            if (it.id == result.data.id) {
                                result.data
                            } else it
                        }
                    )
                        .updateState()
                }
            }
        }

    }

    private fun DirectoriesState.updateState() {
        _state.update {
            this
        }
    }
}
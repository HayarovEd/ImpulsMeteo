package com.edurda77.directories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.AddDevicesGroupUseCase
import com.edurda77.domain.usecase.AddUnitUseCase
import com.edurda77.domain.usecase.DeleteDevicesGroupUseCase
import com.edurda77.domain.usecase.DeleteUnitUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateDevicesGroupUseCase
import com.edurda77.domain.usecase.UpdateUnitUseCase
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.uikit.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DirectoriesViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
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
            loadInitialData()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DirectoriesState()
        )

    fun onEvent(event: DirectoriesEvent) {
        when (event) {
            DirectoriesEvent.Logoff -> {
                viewModelScope.launch { logoffUseCase.invoke() }
            }

            DirectoriesEvent.Refresh -> {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                viewModelScope.launch {
                    delay(1000)
                    loadUnits()
                    loadGroups()
                }
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
        }
    }


    private fun loadInitialData() {
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
                loadGroups()
                loadUnits()
            }
        }
    }

    private suspend fun loadUnits() {
        _state.value.copy(
            isLoading = true
        )
            .updateState()
        when (val result = unitsUseCase.invoke(state.value.token)) {
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
                    units = result.data
                )
                    .updateState()
            }
        }
    }

    private suspend fun loadGroups() {
        _state.value.copy(
            isLoading = true
        )
            .updateState()
        when (val result = devicesGroupsUseCase.invoke(state.value.token)) {
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
                    groups = result.data
                )
                    .updateState()
            }
        }
    }

    private suspend fun insertDevicesGroup(name: String) {
        when (val result = addDevicesGroupUseCase.invoke(
            token = state.value.token,
            name = name,
        )) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadGroups()
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
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadUnits()
            }
        }
    }

    private suspend fun deleteDevicesGroup(id: Int) {
        when (val result = deleteDevicesGroupUseCase.invoke(
            token = state.value.token,
            id = id
        )) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadGroups()
            }
        }
    }

    private suspend fun deleteUnit(id: Int) {
        when (val result = deleteUnitUseCase.invoke(
            token = state.value.token,
            id = id
        )) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadUnits()
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
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadGroups()
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
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadUnits()
            }
        }
    }

    private fun DirectoriesState.updateState() {
        _state.update {
            this
        }
    }
}
package com.edurda77.directories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.UnitsUseCase
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
class DirectoriesViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val logoffUseCase: LogOffUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val unitsUseCase: UnitsUseCase
) : ViewModel() {
    private var _state = MutableStateFlow(DirectoriesState())
    val state = _state.asStateFlow()

    init {
        loadInitialData()
    }


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

            is DirectoriesEvent.SwitshDirectoriesType -> {
                _state.value.copy(
                    directoriesType = event.directoriesType,
                )
                    .updateState()
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

    private fun DirectoriesState.updateState() {
        _state.update {
            this
        }
    }
}
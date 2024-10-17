package com.edurda77.users_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.usecase.AddUserUseCase
import com.edurda77.domain.usecase.DeleteUserUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.PermissionsUseCase
import com.edurda77.domain.usecase.UpdateUserUseCase
import com.edurda77.domain.usecase.UsersUseCase
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
class UsersViewModel @Inject constructor(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val logoffUseCase: LogOffUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val usersUseCase: UsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(UsersState())
    val state = _state.asStateFlow()

    init {
        loadLocalData()
    }

    fun onEvent(event: UsersEvent) {
        when (event) {
            UsersEvent.Logoff -> {
                viewModelScope.launch { logoffUseCase.invoke() }
            }

            UsersEvent.Refresh -> {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                viewModelScope.launch {
                    delay(1000)
                    loadUsers()
                }
            }

            UsersEvent.ClearSelected -> {
                _state.value.copy(
                    selectedPermissions = emptyList(),
                    selectedDevices = emptyList()
                )
                    .updateState()
            }

            is UsersEvent.InsertNewUser -> {
                viewModelScope.launch {
                    insertUser(
                        name = event.name,
                        password = event.password,
                        email = event.email,
                        devices = event.devices,
                        permissions = event.permissions
                    )
                }
            }

            is UsersEvent.UpdateSelectedDevice -> {
                val updatedDevices = state.value.selectedDevices.toMutableList()
                if (state.value.selectedDevices.contains(event.deviceUser)) {
                    updatedDevices.remove(event.deviceUser)
                } else {
                    updatedDevices.add(event.deviceUser)
                }
                _state.value.copy(
                    selectedDevices = updatedDevices
                )
                    .updateState()
            }

            is UsersEvent.UpdateSelectedPermission -> {
                val updatedPermissions = state.value.selectedPermissions.toMutableList()
                if (state.value.selectedPermissions.contains(event.permissionUser)) {
                    updatedPermissions.remove(event.permissionUser)
                } else {
                    updatedPermissions.add(event.permissionUser)
                }
                _state.value.copy(
                    selectedPermissions = updatedPermissions
                )
                    .updateState()
            }

            is UsersEvent.DeleteUser -> {
                viewModelScope.launch {
                    when (val result = deleteUserUseCase.invoke(
                        token = state.value.token,
                        id = event.id
                    )) {
                        is ResultWork.Error -> {
                            _state.value.copy(
                                message = result.error.asUiText()
                            )
                                .updateState()
                        }

                        is ResultWork.Success -> {
                            loadUsers()
                        }
                    }
                }
            }

            is UsersEvent.UpdateUser -> {
                viewModelScope.launch {
                    updateUser(
                        id = event.id,
                        name = event.name,
                        password = event.password,
                        email = event.email,
                        devices = event.devices,
                        permissions = event.permissions
                    )
                }
            }

            is UsersEvent.UpdateSelected -> {
                _state.value.copy(
                    selectedDevices = event.user.devices,
                    selectedPermissions = event.user.permissions
                )
                    .updateState()
            }
        }
    }


    private suspend fun insertUser(
        name: String,
        password: String,
        email: String,
        devices: List<DeviceUser>,
        permissions: List<PermissionUser>
    ) {
        when (val result = addUserUseCase.invoke(
            token = state.value.token,
            devices = devices.map { it.id.toString() },
            permissions = permissions.map { it.id.toString() },
            email = email,
            name = name,
            password = password
        )) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadUsers()
            }
        }
    }

    private suspend fun updateUser(
        id: Int,
        name: String,
        password: String,
        email: String,
        devices: List<DeviceUser>,
        permissions: List<PermissionUser>
    ) {
        when (val result = updateUserUseCase.invoke(
            id = id,
            token = state.value.token,
            devices = devices.map { it.id.toString() },
            permissions = permissions.map { it.id.toString() },
            email = email,
            name = name,
            password = password
        )) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                loadUsers()
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
                loadPermissionsAndDevices()
                loadUsers()
            }
        }
    }

    private suspend fun loadUsers() {
        _state.value.copy(
            isLoading = true
        )
            .updateState()
        when (val result = usersUseCase.invoke(state.value.token)) {
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
                    users = result.data
                )
                    .updateState()
            }
        }
    }

    private suspend fun loadPermissionsAndDevices() {
        when (val result = permissionsUseCase.invoke(state.value.token)) {
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
                    permissions = result.data.permissions,
                    devices = result.data.devicesPermission
                )
                    .updateState()
            }
        }
    }


    private fun UsersState.updateState() {
        _state.update {
            this
        }
    }
}
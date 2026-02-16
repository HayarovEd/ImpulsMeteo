package com.edurda77.users_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.newModels.UserUi
import com.edurda77.domain.model.newModels.WebSocketMessage
import com.edurda77.domain.usecase.AddUserUseCase
import com.edurda77.domain.usecase.CloseWebsocketUseCase
import com.edurda77.domain.usecase.DeleteUserUseCase
import com.edurda77.domain.usecase.DevicesUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.PermissionsUseCase
import com.edurda77.domain.usecase.UpdateUserUseCase
import com.edurda77.domain.usecase.UsersUseCase
import com.edurda77.domain.usecase.WebSocketUseCase
import com.edurda77.domain.usecase.WsMessageFactory
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.resources.uikit.asUiText
import com.edurda77.users_list.mapper.convertToDeviceUser
import com.edurda77.users_list.mapper.convertToPermissionUser
import com.edurda77.users_list.mapper.convertToUserUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UsersViewModel(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val logoffUseCase: LogOffUseCase,
    private val permissionsUseCase: PermissionsUseCase,
    private val usersUseCase: UsersUseCase,
    private val devicesUseCase: DevicesUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val closeWebsocketUseCase: CloseWebsocketUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(UsersState())
    val state = _state
        .onStart {
            loadUserData()
            loadPermissionsAndDevices()
            updateFromWs()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UsersState()
        )


    private val _eventFlow = Channel<UiUsersEvents>()
    val eventFlow = _eventFlow.receiveAsFlow()


    fun onEvent(event: UsersEvent) {
        when (event) {
            UsersEvent.Logoff -> {
                viewModelScope.launch {
                    logoffUseCase.invoke()
                    _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                }
            }

            UsersEvent.Refresh -> {
                loadUsers()
                loadPermissionsAndDevices()
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
                        userId = event.id
                    )) {
                        is ResultWork.Error -> {
                            viewModelScope.launch {
                                _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                            }
                        }

                        is ResultWork.Success -> {
                            _state.value.copy(
                                users = WsMessageFactory.deleteUser(
                                    users = state.value.users,
                                    id = event.id
                                )
                            )
                                .updateState()
                        }
                    }
                }
            }

            is UsersEvent.UpdateUser -> {
                updateUser(
                    id = event.id,
                    name = event.name,
                    password = event.password,
                    email = event.email,
                    devices = event.devices,
                    permissions = event.permissions
                )
            }

            is UsersEvent.UpdateSelected -> {
                _state.value.copy(
                    selectedDevices = event.user.devices,
                    selectedPermissions = event.user.permissions
                )
                    .updateState()
            }

            is UsersEvent.ExpandUser -> {
                val newExpandedUser = state.value.users[event.index].copy(
                    isExpanded = !state.value.users[event.index].isExpanded
                )
                val newList = state.value.users.toMutableList()
                newList[event.index] = newExpandedUser
                _state.value.copy(
                    users = newList
                )
                    .updateState()
            }

            is UsersEvent.SearchUser -> {
                _state.value.copy(
                    query = event.query,
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
            devices = devices,
            permissions = permissions,
            email = email,
            name = name,
            password = password
        )) {
            is ResultWork.Error -> {
                viewModelScope.launch {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    users = state.value.users + result.data.convertToUserUi(),
                )
                    .updateState()
            }
        }
    }

    private fun updateUser(
        id: String,
        name: String,
        password: String,
        email: String,
        devices: List<DeviceUser>,
        permissions: List<PermissionUser>
    ) {
        viewModelScope.launch {
            when (val result = updateUserUseCase.invoke(
                UserUi(
                    id = id,
                    devices = devices,
                    email = email,
                    isEnabled = true,
                    name = name,
                    password = password,
                    permissions = permissions
                )
            )) {
                is ResultWork.Error -> {
                    viewModelScope.launch {
                        if (result.error is DataError.TokenError) {
                            _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                        } else {
                            _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                            _state.value.copy(
                                isLoading = false,
                            )
                                .updateState()
                        }
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        users = WsMessageFactory.updateUser(
                            users = state.value.users,
                            newUser = result.data.convertToUserUi()
                        ),
                    )
                        .updateState()
                }
            }
        }
    }


    private fun loadUsers() {
        _state.value.copy(
            isLoading = true
        )
            .updateState()
        viewModelScope.launch {
            when (val result = usersUseCase.invoke()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoading = false,
                        users = result.data.map { it.convertToUserUi() }
                    )
                        .updateState()
                }
            }
        }
    }

    private fun loadPermissionsAndDevices() {
        viewModelScope.launch {
            when (val result = permissionsUseCase.invoke()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        permissions = result.data.map { it.convertToPermissionUser() },
                    )
                        .updateState()
                }
            }
        }
        viewModelScope.launch {
            when (val result = devicesUseCase.invoke()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        devices = result.data.map { it.convertToDeviceUser() },
                    )
                        .updateState()
                }
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
                        _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiUsersEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        authUser = result.data,
                        // isLoading = false,
                    )
                        .updateState()
                    loadUsers()
                }
            }
        }
    }

    private fun updateFromWs() {
        viewModelScope.launch {
            webSocketUseCase.invoke().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {}
                    is ResultWork.Success -> {
                        when (val successResult = collector.data) {
                            is WebSocketMessage.DeviceCreate -> {
                                _state.value.copy(
                                    devices = state.value.devices + successResult.device.convertToDeviceUser(),
                                )
                                    .updateState()
                            }

                            is WebSocketMessage.DeviceDelete -> {
                                _state.value.copy(
                                    devices = WsMessageFactory.deleteDeviceUser(
                                        devices = state.value.devices,
                                        id = successResult.id
                                    )
                                )
                                    .updateState()
                            }

                            is WebSocketMessage.DeviceUpdate -> {
                                _state.value.copy(
                                    devices = WsMessageFactory.updateDeviceUser(
                                        devices = state.value.devices,
                                        device = successResult.device.convertToDeviceUser()
                                    )
                                )
                                    .updateState()
                            }

                            is WebSocketMessage.UserCreate -> {
                                _state.value.copy(
                                    users = state.value.users + successResult.user.convertToUserUi(),
                                )
                                    .updateState()
                            }

                            is WebSocketMessage.UserDelete -> {
                                _state.value.copy(
                                    users = WsMessageFactory.deleteUser(
                                        users = state.value.users,
                                        id = successResult.id
                                    ),
                                )
                                    .updateState()
                                state.value.authUser?.let { user ->
                                    if (successResult.id == user.id) {
                                        _eventFlow.send(UiUsersEvents.LoginNavigationEvent)
                                    }
                                }
                            }

                            is WebSocketMessage.UserUpdate -> {
                                _state.value.copy(
                                    users = WsMessageFactory.updateUser(
                                        users = state.value.users,
                                        newUser = successResult.user.convertToUserUi()
                                    ),
                                )
                                    .updateState()
                                state.value.authUser?.let { user ->
                                    if (successResult.user.id == user.id) {
                                        _state.value.copy(
                                            authUser = WsMessageFactory.updateAuthUser(
                                                authUser = user,
                                                newUser = successResult.user
                                            )
                                        )
                                            .updateState()
                                    }
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }


    private fun UsersState.updateState() {
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
package com.edurda77.users_list


import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.PermissionUserOld
import com.edurda77.resources.uikit.UiText
import com.edurda77.users_list.model.UserUi

data class UsersState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val loggedUser: LoggedUser? = null,
    val permissions: List<PermissionUserOld> = emptyList(),
    val devices: List<DeviceUser> = emptyList(),
    val selectedPermissions: List<PermissionUserOld> = emptyList(),
    val selectedDevices: List<DeviceUser> = emptyList(),
    val users: List<UserUi> = emptyList(),
    val query: String = "",
)

package com.edurda77.users_list


import com.edurda77.domain.model.DevicePermission
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.Permission
import com.edurda77.domain.model.User
import com.edurda77.resources.uikit.UiText

data class UsersState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val loggedUser: LoggedUser? = null,
    val permissions: List<Permission> = emptyList(),
    val devices: List<DevicePermission> = emptyList(),
    val users: List<User> = emptyList(),
)

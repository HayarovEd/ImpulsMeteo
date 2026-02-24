package com.edurda77.users_list


import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.AuthUser
import com.edurda77.resources.uikit.UiText
import com.edurda77.domain.model.UserUi

data class UsersState(
    val isLoading: Boolean = false,
    val token: String = "",
    val permissions: List<PermissionUser> = emptyList(),
    val devices: List<DeviceUser> = emptyList(),
    val selectedPermissions: List<PermissionUser> = emptyList(),
    val selectedDevices: List<DeviceUser> = emptyList(),
    val users: List<UserUi> = emptyList(),
    val query: String = "",
    val authUser: AuthUser? = null,
) {
    val filteredUsers = users.filter {
        it.name.contains(
            other = query,
            ignoreCase = true
        )
    }
}

sealed interface UiUsersEvents {
    data object LoginNavigationEvent : UiUsersEvents
    data class OnError(val message: UiText) : UiUsersEvents
}



package com.edurda77.users_list

import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser


sealed class UsersEvent {
    data object Refresh : UsersEvent()
    data object Logoff : UsersEvent()
    class UpdateSelectedPermission(val permissionUser: PermissionUser) : UsersEvent()
    class UpdateSelectedDevice(val deviceUser: DeviceUser) : UsersEvent()
    class InsertNewUser(
        val email: String,
        val name: String,
        val password: String,
        val devices: List<DeviceUser>,
        val permissions: List<PermissionUser>,
    ) : UsersEvent()

    data object ClearSelected : UsersEvent()
    class DeleteUser(val id: Int) : UsersEvent()
}
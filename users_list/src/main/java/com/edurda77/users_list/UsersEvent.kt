package com.edurda77.users_list

import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUserOld
import com.edurda77.users_list.model.UserUi


sealed class UsersEvent {
    data object Refresh : UsersEvent()
    data object Logoff : UsersEvent()
    class UpdateSelectedPermission(val permissionUserOld: PermissionUserOld) : UsersEvent()
    class UpdateSelectedDevice(val deviceUser: DeviceUser) : UsersEvent()
    class InsertNewUser(
        val email: String,
        val name: String,
        val password: String,
        val devices: List<DeviceUser>,
        val permissions: List<PermissionUserOld>,
    ) : UsersEvent()

    class UpdateUser(
        val id: Int,
        val email: String,
        val name: String,
        val password: String,
        val devices: List<DeviceUser>,
        val permissions: List<PermissionUserOld>,
    ) : UsersEvent()

    data object ClearSelected : UsersEvent()
    class DeleteUser(val id: Int) : UsersEvent()
    class UpdateSelected(val user: UserUi) : UsersEvent()
    class ExpandUser(val index: Int) : UsersEvent()
    class SearchUser(val query:String) : UsersEvent()
}
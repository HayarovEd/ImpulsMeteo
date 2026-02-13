package com.edurda77.users_list.mapper

import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.Permission
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.model.newModels.UserUi


fun User.convertToUserUi(): UserUi {
    return UserUi(
        email = this.email,
        id = this.id,
        name = this.name,
        devices = this.devices.map { it.convertToDeviceUser() },
        permissions = this.permissions.map { it.convertToPermissionUser() },
        isEnabled = isEnabled,
        password = password
    )
}

fun Device.convertToDeviceUser(): DeviceUser {
    return DeviceUser(
        name = this.name,
        id = id
    )
}

fun Permission.convertToPermissionUser(): PermissionUser {
    return PermissionUser(
        displayName = this.displayName,
        id = id
    )
}
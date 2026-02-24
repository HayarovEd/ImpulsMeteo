package com.edurda77.users_list.mapper

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.Permission
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.User
import com.edurda77.domain.model.UserUi


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
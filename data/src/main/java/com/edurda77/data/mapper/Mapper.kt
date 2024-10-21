package com.edurda77.data.mapper

import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.data.remote.auth_user.AuthUserDto
import com.edurda77.data.remote.device.BodyDeviceDto
import com.edurda77.data.remote.device.NotificationDto
import com.edurda77.data.remote.devices.DevicesDto
import com.edurda77.data.remote.group.DevicesGropusDto
import com.edurda77.data.remote.permission.PermissionsDto
import com.edurda77.data.remote.units.UnitsDto
import com.edurda77.data.remote.user.UsersDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.Permissions
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.model.User
import com.edurda77.domain.utils.DEVICES_LIST
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.STATUS_ON
import com.edurda77.domain.utils.USERS_LIST
import com.edurda77.domain.utils.convertToLocalDateTime
import kotlinx.serialization.json.Json


fun AuthDto.convertToAuth(): Auth {
    return Auth(
        accessToken = this.accessToken,
        expiresAt = convertToLocalDateTime(this.expiresAt),
        id = if (this.permissions.isNotEmpty()) {
            val containsRequired =
                this.permissions.any { it.id == USERS_LIST || it.id == DEVICES_LIST || it.id == DIRECTORY_LIST }
            if (containsRequired)
                this.permissions.first().id else NEGATIVE_ID
        } else NEGATIVE_ID
    )
}

fun AuthUserDto.convertToLoggedUser(): LoggedUser {
    return LoggedUser(
        id = this.permissions.first().pivot.userId,
        name = this.name,
        email = this.email,
        permissions = this.permissions.map { it.id }
    )
}

fun DevicesGropusDto.convertToGroups(): List<GroupDevices> {
    return this.groupsDto.map {
        GroupDevices(
            id = it.id,
            name = it.name
        )
    }
}

fun DevicesDto.convertToDevices(): List<Device> {
    return this.deviceDto.map {
        Device(
            id = it.id,
            name = it.name,
            key = it.key,
            status = it.status == STATUS_ON,
            video = it.video,
            groups = it.groups.map { group ->
                GroupDevices(
                    id = group.id,
                    name = group.name
                )
            },
            params = it.paramDtos.map { param ->
                Param(
                    classIcon = param.classIcon,
                    name = param.name,
                    label = param.label,
                    value = param.value.toDoubleOrNull() ?: 0.0,
                    idUnit = param.idUnit,
                    id = param.id
                )
            },
            updatedAt = it.lastUpdate ?: ""
        )
    }
}

fun PermissionsDto.convertToPermissions(): Permissions {
    return Permissions(
        permissions = this.permissionsDto.map {
            PermissionUser(
                displayName = it.displayName,
                id = it.id
            )
        },
        devicesPermission = this.devicePermissionsDto.map {
            DeviceUser(
                id = it.id,
                name = it.name
            )
        }
    )
}

fun UsersDto.convertToUsers(): List<User> {
    return this.userDto.map { user ->
        User(
            email = user.email,
            id = user.id,
            name = user.name,
            devices = user.devicesUserDto.map {
                DeviceUser(
                    id = it.id,
                    name = it.name
                )
            },
            permissions = user.permissionsUserDto.map {
                PermissionUser(
                    displayName = it.displayName,
                    id = it.id
                )
            }
        )
    }
}

fun UnitsDto.convertToUnits(): List<UnitMeteo> {
    return this.units.map {
        UnitMeteo(
            id = it.id,
            name = it.name,
            short = it.short
        )
    }
}

private val json = Json { ignoreUnknownKeys = true }

fun BodyDeviceDto.convertToSingleDevice(): SingleDevice {
    return SingleDevice(
        id = this.deviceDto.first().id,
        name = this.deviceDto.first().name,
        key = this.deviceDto.first().key,
        status = this.deviceDto.first().status == STATUS_ON,
        video = this.deviceDto.first().video,
        updatedAt = this.deviceDto.first().lastUpdate ?: "",
        groups = this.deviceDto.first().groups.map {
            GroupDevices(
                id = it.id,
                name = it.name
            )
        },
        params = this.deviceDto.first().params.map {
            Param(
                classIcon = it.classIcon,
                name = it.name,
                label = it.label,
                value = it.value.toDoubleOrNull() ?: 0.0,
                idUnit = it.idUnit,
                id = it.id
            )
        },
        notifications = this.deviceDto.first().notificationsDto.params.map {
            val notificationDto = json.decodeFromString<NotificationDto>(it)
            NotificationDevice(
                id = notificationDto.id,
                condition = notificationDto.condition,
                idParam = notificationDto.idParam,
                value = notificationDto.value
            )
        }
    )
}


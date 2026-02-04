package com.edurda77.data.mapper

import com.edurda77.data.remote.auth.AuthDto
import com.edurda77.data.remote.auth_user.AuthUserDto
import com.edurda77.data.remote.device.BodyDeviceDto
import com.edurda77.data.remote.devices.DevicesDto
import com.edurda77.data.remote.devices.ParamDto
import com.edurda77.data.remote.favorite.FavoriteDto
import com.edurda77.data.remote.group.DevicesGropusDto
import com.edurda77.data.remote.history.ResponseHistory
import com.edurda77.data.remote.permission.PermissionsDto
import com.edurda77.data.remote.units.UnitsDto
import com.edurda77.data.remote.update_device.UpdateDeviceDto
import com.edurda77.data.remote.update_device.UpdateDeviceNotificationsDto
import com.edurda77.data.remote.update_device.UpdateDeviceNotificationsParamsDto
import com.edurda77.data.remote.update_device.UpdateDeviceParamsDto
import com.edurda77.data.remote.update_notifications.UpdateNotificationsDto
import com.edurda77.data.remote.update_notifications.UpdateParamsNotificationsDto
import com.edurda77.data.remote.user.UsersDto
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.ElementHistory
import com.edurda77.domain.model.Favorite
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.model.Notifications
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.Permissions
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.model.User
import com.edurda77.domain.utils.DEVICES_LIST
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.domain.utils.IS_HIDDEN
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.STATUS_ON
import com.edurda77.domain.utils.USERS_LIST
import com.edurda77.domain.utils.convertToLocalDateTime

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
    return this.deviceDto.map { device ->
        Device(
            id = device.id,
            name = device.name,
            key = device.key,
            status = device.status == STATUS_ON,
            video = device.video,
            groups = device.groups.map { group ->
                GroupDevices(
                    id = group.id,
                    name = group.name
                )
            },
            params = device.paramDtos.map { param ->
                Param(
                    classIcon = param.classIcon,
                    name = param.name,
                    label = param.label,
                    value = param.value.toDoubleOrNull() ?: 0.0,
                    idUnit = param.idUnit,
                    id = param.id,
                    isHidden = param.isHidden == IS_HIDDEN,
                    color = param.color,
                    idDevice = param.idDevice
                )
            }.filter { !it.isHidden },
            updatedAt = device.lastUpdate ?: "",
            statusNotifications = device.notifications.deviceStatus,
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


fun BodyDeviceDto.convertToSingleDevice(): SingleDevice {
    return SingleDevice(
        id = this.singleDeviceDto.first().id,
        name = this.singleDeviceDto.first().name,
        key = this.singleDeviceDto.first().key,
        status = this.singleDeviceDto.first().status == STATUS_ON,
        video = this.singleDeviceDto.first().video,
        frequency = this.singleDeviceDto.first().update,
        updatedAt = this.singleDeviceDto.first().lastUpdate,
        host = this.singleDeviceDto.first().host ?: "",
        port = this.singleDeviceDto.first().port,
        groups = this.singleDeviceDto.first().groups.map {
            GroupDevices(
                id = it.id,
                name = it.name
            )
        },
        params = this.singleDeviceDto.first().params.map {
            Param(
                classIcon = it.classIcon,
                name = it.name,
                label = it.label,
                value = it.value.toDoubleOrNull() ?: 0.0,
                idUnit = it.idUnit,
                id = it.id,
                isHidden = it.isHidden == IS_HIDDEN,
                color = it.color,
                idDevice = it.idDevice
            )
        },
        notifications = Notifications(
            deviceStatus = this.singleDeviceDto.first().notificationsDto.deviceStatus,
            notifications = this.singleDeviceDto.first().notificationsDto.paramNotifications.map {
                NotificationDevice(
                    id = it.id,
                    condition = it.condition,
                    idParam = it.idParam,
                    value = it.value
                )
            },
        )
    )
}

fun Notifications.convertToUpdateNotificationsDto(): UpdateNotificationsDto {
    return UpdateNotificationsDto(
        deviceStatus = this.deviceStatus,
        params = this.notifications.map {
            UpdateParamsNotificationsDto(
                condition = it.condition,
                id = it.id,
                idParam = it.idParam,
                value = it.value
            )
        }
    )
}

fun SingleDevice.convertToSingleDeviceDto(): UpdateDeviceDto {
    return UpdateDeviceDto(
        groups = this.groups.map {
            it.id
        },
        host = this.host,
        id = this.id,
        key = this.key,
        lastUpdate = "",
        name = this.name,
        notifications = UpdateDeviceNotificationsDto(
            deviceStatus = this.notifications.deviceStatus,
            params = this.notifications.notifications.map {
                UpdateDeviceNotificationsParamsDto(
                    condition = it.condition,
                    id = it.id ?: 0,
                    idParam = it.idParam,
                    value = it.value
                )
            }
        ),
        port = this.port,
        params = this.params.map {
            UpdateDeviceParamsDto(
                classIcon = it.classIcon,
                id = it.id,
                idDevice = this.id,
                color = it.color,
                idUnit = it.idUnit,
                isHidden = if (!it.isHidden) 0 else 1,
                label = it.label,
                name = it.name,
                value = it.value.toString()
            )
        },
        status = if (this.status) "on" else "off",
        update = this.frequency,
        video = this.video,
        cameras = emptyList()
    )
}

fun Param.convertToParamDto(): ParamDto {
    return ParamDto(
        classIcon = this.classIcon,
        id = this.id,
        idDevice = this.id,
        color = this.color,
        idUnit = this.idUnit,
        isHidden = if (!this.isHidden) 0 else 1,
        label = this.label,
        name = this.name,
        value = this.value.toString()
    )
}


fun ResponseHistory.convertToElementsHistory(): List<List<ElementHistory>> {
    return this.elementsHistory.map { baseHistory ->
        baseHistory.map {
            ElementHistory(
                time = convertToLocalDateTime(it.time),
                value = it.value
            )
        }
    }
}

fun FavoriteDto.convertToFavorite(): Favorite {
    return Favorite(deviceId = this.deviceId)
}



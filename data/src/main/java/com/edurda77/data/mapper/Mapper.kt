package com.edurda77.data.mapper

import com.edurda77.data.remote.auth.AuthDtoOld
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
import com.edurda77.data.remote.user.UsersDtoOld
import com.edurda77.domain.model.AuthOld
import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.History
import com.edurda77.domain.model.FavoriteOld
import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.NotificationDeviceOld
import com.edurda77.domain.model.NotificationsOld
import com.edurda77.domain.model.ParamOld
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.PermissionsOld
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteoOld
import com.edurda77.domain.model.UserOld
import com.edurda77.domain.utils.DEVICES_LIST
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.domain.utils.IS_HIDDEN
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.STATUS_ON
import com.edurda77.domain.utils.USERS_LIST
import com.edurda77.domain.utils.convertToLocalDateTimeOld

fun AuthDtoOld.convertToAuth(): AuthOld {
    return AuthOld(
        accessToken = this.accessToken,
        expiresAt = convertToLocalDateTimeOld(this.expiresAt),
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

fun DevicesGropusDto.convertToGroups(): List<GroupDevicesOld> {
    return this.groupsDto.map {
        GroupDevicesOld(
            id = it.id,
            name = it.name
        )
    }
}

fun DevicesDto.convertToDevices(): List<DeviceOld> {
    return this.deviceDto.map { device ->
        DeviceOld(
            id = device.id,
            name = device.name,
            key = device.key,
            status = device.status == STATUS_ON,
            video = device.video,
            groups = device.groups.map { group ->
                GroupDevicesOld(
                    id = group.id,
                    name = group.name
                )
            },
            params = device.paramDtos.map { param ->
                ParamOld(
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

fun PermissionsDto.convertToPermissions(): PermissionsOld {
    return PermissionsOld(
        permissions = this.permissionsDto.map {
            PermissionUser(
                displayName = it.displayName,
                id = "it.id"
            )
        },
        devicesPermission = this.devicePermissionsDto.map {
            DeviceUser(
                id = "it.id",
                name = it.name
            )
        }
    )
}

fun UsersDtoOld.convertToUsers(): List<UserOld> {
    return this.userDtoOld.map { user ->
        UserOld(
            email = user.email,
            id = user.id,
            name = user.name,
            devices = user.devicesUserDto.map {
                DeviceUser(
                    id = "it.id",
                    name = it.name
                )
            },
            permissions = user.permissionsUserDto.map {
                PermissionUser(
                    displayName = it.displayName,
                    id =" it.id"
                )
            }
        )
    }
}

fun UnitsDto.convertToUnits(): List<UnitMeteoOld> {
    return this.units.map {
        UnitMeteoOld(
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
            GroupDevicesOld(
                id = it.id,
                name = it.name
            )
        },
        params = this.singleDeviceDto.first().params.map {
            ParamOld(
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
        notificationsOld = NotificationsOld(
            deviceStatus = this.singleDeviceDto.first().notificationsDto.deviceStatus,
            notifications = this.singleDeviceDto.first().notificationsDto.paramNotifications.map {
                NotificationDeviceOld(
                    id = it.id,
                    condition = it.condition,
                    idParam = it.idParam,
                    value = it.value
                )
            },
        )
    )
}

fun NotificationsOld.convertToUpdateNotificationsDto(): UpdateNotificationsDto {
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
            deviceStatus = this.notificationsOld.deviceStatus,
            params = this.notificationsOld.notifications.map {
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

fun ParamOld.convertToParamDto(): ParamDto {
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


fun ResponseHistory.convertToElementsHistory(): List<List<History>> {
    return this.elementsHistory.map { baseHistory ->
        baseHistory.map {
            History(
                time = convertToLocalDateTimeOld(it.time),
                value = it.value
            )
        }
    }
}

fun FavoriteDto.convertToFavorite(): FavoriteOld {
    return FavoriteOld(deviceId = this.deviceId)
}



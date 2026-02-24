package com.edurda77.data.mapper

import com.edurda77.domain.model.AuthUser
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.Favorite
import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.model.MeasurementUnit
import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.model.NotificationParam
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.Permission
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.User
import com.edurda77.domain.model.UserUi
import com.edurda77.domain.utils.STATUS_ON
import com.edurda77.domain.utils.convertToLocalDateTime

fun com.edurda77.data.remote.permission.PermissionDto.toPermission(): Permission {
    return Permission(
        description = this.description,
        displayName = this.displayName,
        id = this.id,
        name = this.name,
        parent = this.parent
    )
}

fun com.edurda77.data.remote.notification.NotificationsDeviceDtos.toNotificationsDevice(): NotificationParam {
    return NotificationParam(
        condition = this.condition,
        id = this.id,
        isSend = this.isSend,
        paramId = this.paramId,
        userId = this.userId,
        value = this.value
    )
}

fun com.edurda77.data.remote.notification.NotificationDeviceDto.toNotificationDevice(): NotificationDevice {
    return NotificationDevice(
        deviceId = this.deviceId,
        id = this.id,
        notificationParam = this.notificationsDeviceDtos.map { it.toNotificationsDevice() },
        userId = this.userId,
        value = this.value
    )
}


fun NotificationParam.toNotificationsDeviceDtos(): com.edurda77.data.remote.notification.NotificationsDeviceDtos {
    return com.edurda77.data.remote.notification.NotificationsDeviceDtos(
        condition = this.condition,
        isSend = this.isSend,
        paramId = this.paramId,
        userId = this.userId,
        value = this.value,
        id = ""
    )
}

fun com.edurda77.data.remote.favorite.FavoriteDto.toFavorite(): Favorite {
    return Favorite(
        createdAt = this.createdAt,
        deviceId = this.deviceId,
        id = this.id,
        updateAt = this.updateAt,
        userId = this.userId
    )
}

fun com.edurda77.data.remote.param.ParamDto.toParam(): Param {
    return Param(
        id = id,
        classIcon = classIcon,
        color = color,
        isHidden = isHidden,
        label = label,
        name = name,
        value = value,
        measurementUnit = measurementUnitDto.toMeasurementUnit()
    )
}

fun Param.toParamDto(): com.edurda77.data.remote.param.ParamDto {
    return com.edurda77.data.remote.param.ParamDto(
        id = id,
        classIcon = classIcon,
        color = color,
        isHidden = isHidden,
        label = label,
        name = name,
        value = value,
        measurementUnitDto = measurementUnit.toMeasurementUnitDto()
    )
}


fun com.edurda77.data.remote.measurement.MeasurementUnitDto.toMeasurementUnit(): MeasurementUnit {
    return MeasurementUnit(
        id = id,
        abbreviation = abbreviation,
        name = name
    )
}

fun MeasurementUnit.toMeasurementUnitDto(): com.edurda77.data.remote.measurement.MeasurementUnitDto {
    return com.edurda77.data.remote.measurement.MeasurementUnitDto(
        id = id,
        abbreviation = abbreviation,
        name = name
    )
}


fun com.edurda77.data.remote.device.DeviceDto.toDevice(): Device {
    return Device(
        host = this.host?:"",
        id = this.id,
        key = this.key,
        name = this.name,
        notificationDevice = this.notificationDeviceDto?.toNotificationDevice(),
        port = this.port,
        status = this.status == STATUS_ON,
        updateRate = this.updateRate,
        updatedDate = this.updatedDate?.let {
            convertToLocalDateTime(it)
        },
        videoUrl = this.videoUrl,
        groups = groupsDevice.map { it.toGroupDevice() },
        params = params?.map { it.toParam() }?: emptyList()
    )
}

fun com.edurda77.data.remote.device.DeviceWsDto.convertToDevice(): Device {
    return Device(
        host = this.host?:"",
        id = this.id,
        key = this.key,
        name = this.name,
        notificationDevice = null,
        port = this.port,
        status = this.status == STATUS_ON,
        updateRate = this.updateRate,
        updatedDate = convertToLocalDateTime(this.updatedDate),
        videoUrl = this.videoUrl,
        groups = groupsDevice.map { it.toGroupDevice() },
        params = params.map { it.toParam() }
    )
}

fun com.edurda77.data.remote.group.GroupDeviceDto.toGroupDevice(): GroupDevice {
    return GroupDevice(
        id = id,
        name = name
    )
}

fun GroupDevice.toGroupDeviceDto(): com.edurda77.data.remote.group.GroupDeviceDto {
    return com.edurda77.data.remote.group.GroupDeviceDto(
        id = id,
        name = name
    )
}


fun com.edurda77.data.remote.auth.AuthUserDto.toAuthUser(): AuthUser {
    return AuthUser(
        createdAt = this.createdAt,
        devices = this.deviceDtos.map { it.toDevice() },
        email = this.email,
        favorites = this.favoriteDtos.map { it.toFavorite() },
        id = this.id,
        isEnabled = this.isEnabled,
        name = this.name,
        password = this.password,
        permissions = this.permissionDtos.map { it.toPermission() },
        updateAt = convertToLocalDateTime(this.updateAt),
    )
}

fun com.edurda77.data.remote.user.UserDto.toUser(): User {
    return User(
        createdAt = this.createdAt,
        devices = this.devices.map { it.toDevice() },
        email = this.email,
        favorites = this.favorites.map { it.toFavorite() },
        id = this.id,
        isEnabled = this.isEnabled,
        name = this.name,
        password = this.password?:"",
        permissions = this.permissions.map { it.toPermission() },
        updateAt = convertToLocalDateTime(this.updateAt),
    )
}

fun DeviceUser.toDeviceUserRequest(): com.edurda77.data.remote.user.DeviceUserRequest {
    return com.edurda77.data.remote.user.DeviceUserRequest(
        id = id
    )
}
fun PermissionUser.toPermissionUserRequest(): com.edurda77.data.remote.user.PermissionUserRequest {
    return com.edurda77.data.remote.user.PermissionUserRequest(
        id = id
    )
}

fun UserUi.toUserUpdateRequest(): com.edurda77.data.remote.user.UserUpdateRequest {
    return com.edurda77.data.remote.user.UserUpdateRequest(
        deviceUserRequests = this.devices.map { it.toDeviceUserRequest() },
        email = this.email,
        id = this.id,
        isEnabled = this.isEnabled,
        name = this.name,
        password = password.ifBlank { null },
        permissionUserRequests = this.permissions.map { it.toPermissionUserRequest() },
    )
}

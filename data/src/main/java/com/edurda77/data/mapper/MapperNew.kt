package com.edurda77.data.mapper

import com.edurda77.data.remote.newDtos.auth.AuthUserDto
import com.edurda77.data.remote.newDtos.device.DeviceDto
import com.edurda77.data.remote.newDtos.device.DeviceWsDto
import com.edurda77.data.remote.newDtos.favorite.FavoriteDto
import com.edurda77.data.remote.newDtos.group.GroupDeviceDto
import com.edurda77.data.remote.newDtos.measurement.MeasurementUnitDto
import com.edurda77.data.remote.newDtos.notification.NotificationDeviceDto
import com.edurda77.data.remote.newDtos.notification.NotificationsDeviceDtos
import com.edurda77.data.remote.newDtos.param.ParamDto
import com.edurda77.data.remote.newDtos.permission.PermissionDto
import com.edurda77.data.remote.newDtos.user.DeviceUserRequest
import com.edurda77.data.remote.newDtos.user.PermissionUserRequest
import com.edurda77.data.remote.newDtos.user.UserDto
import com.edurda77.data.remote.newDtos.user.UserUpdateRequest
import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.Favorite
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.model.newModels.NotificationDevice
import com.edurda77.domain.model.newModels.NotificationParam
import com.edurda77.domain.model.newModels.Param
import com.edurda77.domain.model.newModels.Permission
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.model.newModels.UserUi
import com.edurda77.domain.utils.STATUS_ON
import com.edurda77.domain.utils.convertToLocalDateTime

fun PermissionDto.toPermission(): Permission {
    return Permission(
        description = this.description,
        displayName = this.displayName,
        id = this.id,
        name = this.name,
        parent = this.parent
    )
}

fun NotificationsDeviceDtos.toNotificationsDevice(): NotificationParam {
    return NotificationParam(
        condition = this.condition,
        id = this.id,
        isSend = this.isSend,
        paramId = this.paramId,
        userId = this.userId,
        value = this.value
    )
}

fun NotificationDeviceDto.toNotificationDevice(): NotificationDevice {
    return NotificationDevice(
        deviceId = this.deviceId,
        id = this.id,
        notificationParam = this.notificationsDeviceDtos.map { it.toNotificationsDevice() },
        userId = this.userId,
        value = this.value
    )
}


fun NotificationParam.toNotificationsDeviceDtos(): NotificationsDeviceDtos {
    return NotificationsDeviceDtos(
        condition = this.condition,
        isSend = this.isSend,
        paramId = this.paramId,
        userId = this.userId,
        value = this.value,
        id = ""
    )
}

fun FavoriteDto.toFavorite(): Favorite {
    return Favorite(
        createdAt = this.createdAt,
        deviceId = this.deviceId,
        id = this.id,
        updateAt = this.updateAt,
        userId = this.userId
    )
}

fun ParamDto.toParam(): Param {
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

fun Param.toParamDto(): ParamDto {
    return ParamDto(
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


fun MeasurementUnitDto.toMeasurementUnit(): MeasurementUnit {
    return MeasurementUnit(
        id = id,
        abbreviation = abbreviation,
        name = name
    )
}

fun MeasurementUnit.toMeasurementUnitDto(): MeasurementUnitDto {
    return MeasurementUnitDto(
        id = id,
        abbreviation = abbreviation,
        name = name
    )
}


fun DeviceDto.toDevice(): Device {
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

fun DeviceWsDto.convertToDevice(): Device {
    return Device(
        host = this.host,
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

fun GroupDeviceDto.toGroupDevice(): GroupDevice {
    return GroupDevice(
        id = id,
        name = name
    )
}

fun GroupDevice.toGroupDeviceDto(): GroupDeviceDto {
    return GroupDeviceDto(
        id = id,
        name = name
    )
}


fun AuthUserDto.toAuthUser(): AuthUser {
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

fun UserDto.toUser(): User {
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

fun DeviceUser.toDeviceUserRequest(): DeviceUserRequest {
    return DeviceUserRequest(
        id = id
    )
}
fun PermissionUser.toPermissionUserRequest(): PermissionUserRequest {
    return PermissionUserRequest(
        id = id
    )
}

fun UserUi.toUserUpdateRequest(): UserUpdateRequest {
    return UserUpdateRequest(
        deviceUserRequests = this.devices.map { it.toDeviceUserRequest() },
        email = this.email,
        id = this.id,
        isEnabled = this.isEnabled,
        name = this.name,
        password = password.ifBlank { null },
        permissionUserRequests = this.permissions.map { it.toPermissionUserRequest()},
    )
}

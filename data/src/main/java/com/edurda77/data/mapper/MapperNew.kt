package com.edurda77.data.mapper

import com.edurda77.data.remote.newDtos.auth.AuthUserDto
import com.edurda77.data.remote.newDtos.auth.DeviceDto
import com.edurda77.data.remote.newDtos.auth.FavoriteDto
import com.edurda77.data.remote.newDtos.auth.NotificationDeviceDto
import com.edurda77.data.remote.newDtos.auth.NotificationsDeviceDtos
import com.edurda77.data.remote.newDtos.auth.PermissionDto
import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.Favorite
import com.edurda77.domain.model.newModels.NotificationDevice
import com.edurda77.domain.model.newModels.NotificationsDevice
import com.edurda77.domain.model.newModels.Permission

fun PermissionDto.toPermission(): Permission {
    return Permission(
        description = this.description,
        displayName = this.displayName,
        id = this.id,
        name = this.name,
        parent = this.parent
    )
}

fun NotificationsDeviceDtos.toNotificationsDevice(): NotificationsDevice {
    return NotificationsDevice(
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
        notificationsDevice = this.notificationsDeviceDtos.map { it.toNotificationsDevice() },
        userId = this.userId,
        value = this.value
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

fun DeviceDto.toDevice(): Device {
    return Device(
        host = this.host,
        id = this.id,
        key = this.key,
        name = this.name,
        notificationDevice = this.notificationDeviceDto.toNotificationDevice(),
        port = this.port,
        status = this.status,
        updateRate = this.updateRate,
        updatedDate = this.updatedDate,
        videoUrl = this.videoUrl
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
        updateAt = this.updateAt
    )
}

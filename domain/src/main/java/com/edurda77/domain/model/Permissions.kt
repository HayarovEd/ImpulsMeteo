package com.edurda77.domain.model

data class Permissions(
    val permissions: List<PermissionUser>,
    val devicesPermission: List<DeviceUser>
)

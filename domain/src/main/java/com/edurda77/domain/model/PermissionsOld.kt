package com.edurda77.domain.model

data class PermissionsOld(
    val permissions: List<PermissionUserOld>,
    val devicesPermission: List<DeviceUser>
)

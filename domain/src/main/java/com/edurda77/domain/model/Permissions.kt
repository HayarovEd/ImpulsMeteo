package com.edurda77.domain.model

data class Permissions(
    val permissions: List<Permission>,
    val devicesPermission: List<DevicePermission>
)

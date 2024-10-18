package com.edurda77.data.remote.permission


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermissionsDto(
    @SerialName("devices")
    val devicePermissionsDto: List<DevicePermissionsDto>,
    @SerialName("permissions")
    val permissionsDto: List<PermissionDto>,
    @SerialName("status")
    val status: String
)
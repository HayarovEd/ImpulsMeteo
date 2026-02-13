package com.edurda77.data.remote.newDtos.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserUpdateRequest(
    @SerialName("id")
    val id: String,
    @SerialName("devices")
    val deviceUserRequests: List<DeviceUserRequest>,
    @SerialName("email")
    val email: String,
    @SerialName("is_enabled")
    val isEnabled: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String?,
    @SerialName("permissions")
    val permissionUserRequests: List<PermissionUserRequest>
)
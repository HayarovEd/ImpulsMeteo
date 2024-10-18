package com.edurda77.data.remote.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("devices")
    val devicesUserDto: List<DeviceUserDto>,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("permissions")
    val permissionsUserDto: List<PermissionUserDto>,
    @SerialName("updated_at")
    val updatedAt: String
)
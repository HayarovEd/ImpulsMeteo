package com.edurda77.data.remote.newDtos.user


import com.edurda77.data.remote.newDtos.device.DeviceDto
import com.edurda77.data.remote.newDtos.favorite.FavoriteDto
import com.edurda77.data.remote.newDtos.permission.PermissionDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("devices")
    val devices: List<DeviceDto>,
    @SerialName("email")
    val email: String,
    @SerialName("favorites")
    val favorites: List<FavoriteDto>,
    @SerialName("id")
    val id: String,
    @SerialName("is_enabled")
    val isEnabled: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String?,
    @SerialName("permissions")
    val permissions: List<PermissionDto>,
    @SerialName("update_at")
    val updateAt: String
)
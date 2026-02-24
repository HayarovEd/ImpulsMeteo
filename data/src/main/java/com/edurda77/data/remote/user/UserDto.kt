package com.edurda77.data.remote.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("devices")
    val devices: List<com.edurda77.data.remote.device.DeviceDto>,
    @SerialName("email")
    val email: String,
    @SerialName("favorites")
    val favorites: List<com.edurda77.data.remote.favorite.FavoriteDto>,
    @SerialName("id")
    val id: String,
    @SerialName("is_enabled")
    val isEnabled: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String?,
    @SerialName("permissions")
    val permissions: List<com.edurda77.data.remote.permission.PermissionDto>,
    @SerialName("update_at")
    val updateAt: String
)
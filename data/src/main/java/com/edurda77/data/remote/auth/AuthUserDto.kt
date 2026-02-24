package com.edurda77.data.remote.auth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthUserDto(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("devices")
    val deviceDtos: List<com.edurda77.data.remote.device.DeviceDto>,
    @SerialName("email")
    val email: String,
    @SerialName("favorites")
    val favoriteDtos: List<com.edurda77.data.remote.favorite.FavoriteDto>,
    @SerialName("id")
    val id: String,
    @SerialName("is_enabled")
    val isEnabled: Boolean,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String?,
    @SerialName("permissions")
    val permissionDtos: List<com.edurda77.data.remote.permission.PermissionDto>,
    @SerialName("update_at")
    val updateAt: String
)
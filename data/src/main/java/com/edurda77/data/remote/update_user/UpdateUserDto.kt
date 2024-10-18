package com.edurda77.data.remote.update_user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserDto(
    @SerialName("devices")
    val devices: List<String>,
    @SerialName("email")
    val email: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String,
    @SerialName("permissions")
    val permissions: List<String>
)
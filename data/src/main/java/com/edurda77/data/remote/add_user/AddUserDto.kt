package com.edurda77.data.remote.add_user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddUserDto(
    @SerialName("devices")
    val devices: List<String>,
    @SerialName("email")
    val email: String,
    @SerialName("name")
    val name: String,
    @SerialName("password")
    val password: String,
    @SerialName("permissions")
    val permissions: List<String>
)
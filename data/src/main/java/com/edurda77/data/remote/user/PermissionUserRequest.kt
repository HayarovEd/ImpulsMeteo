package com.edurda77.data.remote.user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermissionUserRequest(
    @SerialName("id")
    val id: String
)
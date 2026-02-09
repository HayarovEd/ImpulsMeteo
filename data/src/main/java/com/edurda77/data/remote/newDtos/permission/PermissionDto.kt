package com.edurda77.data.remote.newDtos.permission

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PermissionDto(
    @SerialName("description")
    val description: String,
    @SerialName("displayName")
    val displayName: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("parent")
    val parent: Int
)
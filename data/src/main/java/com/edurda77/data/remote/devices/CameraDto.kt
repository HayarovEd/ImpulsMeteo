package com.edurda77.data.remote.devices


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CameraDto(
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("description")
    val description: String,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("public")
    val publicInt: Int,
    @SerialName("updated_at")
    val updatedAt: String?
)
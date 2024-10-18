package com.edurda77.data.remote.units


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UnitDto(
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("short")
    val short: String,
    @SerialName("updated_at")
    val updatedAt: String
)
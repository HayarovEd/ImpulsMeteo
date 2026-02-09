package com.edurda77.data.remote.newDtos.favorite

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("device_id")
    val deviceId: String,
    @SerialName("id")
    val id: String,
    @SerialName("update_at")
    val updateAt: String,
    @SerialName("user_id")
    val userId: String
)
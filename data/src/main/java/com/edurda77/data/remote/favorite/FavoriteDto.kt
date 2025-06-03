package com.edurda77.data.remote.favorite


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteDto(
    @SerialName("device_id")
    val deviceId: Int,
    @SerialName("id")
    val id: Int,
    @SerialName("user_id")
    val userId: Int
)
package com.edurda77.data.remote.favorite


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteCreateRequest(
    @SerialName("device_id")
    val deviceId: String
)
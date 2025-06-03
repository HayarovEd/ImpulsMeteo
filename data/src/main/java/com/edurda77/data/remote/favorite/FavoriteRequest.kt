package com.edurda77.data.remote.favorite


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteRequest(
    @SerialName("device_id")
    val deviceId: Int
)
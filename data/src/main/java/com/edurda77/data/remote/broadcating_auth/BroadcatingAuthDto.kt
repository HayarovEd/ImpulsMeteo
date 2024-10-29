package com.edurda77.data.remote.broadcating_auth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BroadcatingAuthDto(
    @SerialName("auth")
    val auth: String
)
package com.edurda77.data.remote.newDtos.requests

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RefreshRequest(
    @SerialName("refresh_token")
    val refreshToken: String
)
package com.edurda77.data.remote.auth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthDtoOld(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("email")
    val email: String,
    @SerialName("expires_at")
    val expiresAt: String,
    @SerialName("name")
    val name: String,
    @SerialName("permissions")
    val permissions: List<Permission>,
    @SerialName("token_type")
    val tokenType: String
)
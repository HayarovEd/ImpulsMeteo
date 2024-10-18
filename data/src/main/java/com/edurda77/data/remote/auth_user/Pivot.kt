package com.edurda77.data.remote.auth_user


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pivot(
    @SerialName("permission_id")
    val permissionId: Int,
    @SerialName("user_id")
    val userId: Int
)
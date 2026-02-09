package com.edurda77.data.remote.newDtos.notification


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationsDeviceDtos(
    @SerialName("condition")
    val condition: String,
    @SerialName("id")
    val id: String,
    @SerialName("is_send")
    val isSend: Boolean,
    @SerialName("param_id")
    val paramId: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("value")
    val value: Int
)
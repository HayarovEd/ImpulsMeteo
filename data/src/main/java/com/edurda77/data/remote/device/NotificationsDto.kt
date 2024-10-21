package com.edurda77.data.remote.device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationsDto(
    @SerialName("device_status")
    val deviceStatus: Boolean,
    @SerialName("params")
    val params: List<String>
)
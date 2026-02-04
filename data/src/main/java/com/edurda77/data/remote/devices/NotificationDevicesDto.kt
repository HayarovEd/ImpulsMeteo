package com.edurda77.data.remote.devices

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDevicesDto(
    @SerialName("device_status")
    val deviceStatus: Boolean,
)

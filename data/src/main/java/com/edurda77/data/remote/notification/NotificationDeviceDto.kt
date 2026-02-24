package com.edurda77.data.remote.notification


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDeviceDto(
    @SerialName("device_id")
    val deviceId: String,
    @SerialName("id")
    val id: String,
    @SerialName("notifications_device")
    val notificationsDeviceDtos: List<NotificationsDeviceDtos>,
    @SerialName("user_id")
    val userId: String,
    @SerialName("value")
    val value: Boolean
)
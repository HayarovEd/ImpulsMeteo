package com.edurda77.data.remote.newDtos.auth


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    @SerialName("host")
    val host: String,
    @SerialName("id")
    val id: String,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("notification_device")
    val notificationDeviceDto: NotificationDeviceDto,
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: String,
    @SerialName("update_rate")
    val updateRate: Int,
    @SerialName("updated_date")
    val updatedDate: String,
    @SerialName("video_url")
    val videoUrl: String
)
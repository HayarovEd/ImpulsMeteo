package com.edurda77.data.remote.update_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceDto(
    @SerialName("cameras")
    val cameras: List<String?>,
    @SerialName("groups")
    val groups: List<Int>,
    @SerialName("host")
    val host: String,
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("lastUpdate")
    val lastUpdate: String,
    @SerialName("name")
    val name: String,
    @SerialName("notifications")
    val notifications: UpdateDeviceNotificationsDto,
    @SerialName("params")
    val params: List<UpdateDeviceParamsDto>,
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: String,
    @SerialName("update")
    val update: Int,
    @SerialName("video")
    val video: String?
)
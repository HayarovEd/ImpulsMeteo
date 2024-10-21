package com.edurda77.data.remote.device


import com.edurda77.data.remote.devices.CameraDto
import com.edurda77.data.remote.devices.ParamDto
import com.edurda77.data.remote.group.GroupDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SingleDeviceDto(
    @SerialName("cameras")
    val cameras: List<CameraDto>,
    @SerialName("groups")
    val groups: List<GroupDto>,
    @SerialName("host")
    val host: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("lastUpdate")
    val lastUpdate: String? = "",
    @SerialName("name")
    val name: String,
    @SerialName("notifications")
    val notificationsDto: NotificationsDto,
    @SerialName("params")
    val params: List<ParamDto>,
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: String,
    @SerialName("update")
    val update: Int,
    @SerialName("video")
    val video: String?
)
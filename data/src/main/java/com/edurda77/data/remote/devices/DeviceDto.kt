package com.edurda77.data.remote.devices


import com.edurda77.data.remote.group.GroupDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeviceDto(
    @SerialName("cameras")
    val cameraDtos: List<CameraDto>,
    @SerialName("groups")
    val groups: List<GroupDto>,
    @SerialName("host")
    val host: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("key")
    val key: String,
    @SerialName("lastUpdate")
    val lastUpdate: String,
    @SerialName("name")
    val name: String,
    @SerialName("params")
    val paramDtos: List<ParamDto>,
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: String,
    @SerialName("update")
    val update: Int,
    @SerialName("video")
    val video: String?
)
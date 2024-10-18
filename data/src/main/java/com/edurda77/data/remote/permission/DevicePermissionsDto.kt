package com.edurda77.data.remote.permission


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DevicePermissionsDto(
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
    @SerialName("port")
    val port: Int,
    @SerialName("status")
    val status: String,
    @SerialName("update")
    val update: Int,
    @SerialName("video")
    val video: String?
)
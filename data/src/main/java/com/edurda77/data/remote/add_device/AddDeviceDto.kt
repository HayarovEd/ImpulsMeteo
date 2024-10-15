package com.edurda77.data.remote.add_device

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AddDeviceDto(
    @SerialName("groups")
    val groups: List<Int>,
    @SerialName("key")
    val key: String,
    @SerialName("name")
    val name: String,
    @SerialName("update")
    val update: String
)
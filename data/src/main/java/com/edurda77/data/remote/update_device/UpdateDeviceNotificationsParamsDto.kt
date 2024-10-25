package com.edurda77.data.remote.update_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceNotificationsParamsDto(
    @SerialName("condition")
    val condition: String,
    @SerialName("id")
    val id: Int,
    @SerialName("id_param")
    val idParam: Int,
    @SerialName("value")
    val value: Int
)
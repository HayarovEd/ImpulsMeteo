package com.edurda77.data.remote.update_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateDeviceNotificationsDto(
    @SerialName("device_status")
    val deviceStatus: Boolean,
    @SerialName("params")
    val params: List<UpdateDeviceNotificationsParamsDto>
)
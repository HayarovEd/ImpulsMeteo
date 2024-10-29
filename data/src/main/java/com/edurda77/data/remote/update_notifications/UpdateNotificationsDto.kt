package com.edurda77.data.remote.update_notifications


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateNotificationsDto(
    @SerialName("device_status")
    val deviceStatus: Boolean,
    @SerialName("params")
    val params: List<UpdateParamsNotificationsDto>
)
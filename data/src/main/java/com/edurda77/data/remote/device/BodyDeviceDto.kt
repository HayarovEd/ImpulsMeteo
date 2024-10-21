package com.edurda77.data.remote.device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BodyDeviceDto(
    @SerialName("data")
    val deviceDto: List<SingleDeviceDto>,
    @SerialName("status")
    val status: String
)
package com.edurda77.data.remote.add_device

import com.edurda77.domain.utils.convertToStringDateTime
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
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
    val update: String,
    @SerialName("lastUpdate")
    val lastUpdate: String = convertToStringDateTime(
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    )
)
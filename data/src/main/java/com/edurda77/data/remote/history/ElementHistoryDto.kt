package com.edurda77.data.remote.history


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ElementHistoryDto(
    @SerialName("time")
    val time: String,
    @SerialName("value")
    val value: Double
)
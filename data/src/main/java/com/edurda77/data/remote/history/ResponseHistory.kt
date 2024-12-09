package com.edurda77.data.remote.history


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResponseHistory(
    @SerialName("data")
    val elementsHistory: List<List<ElementHistoryDto>>
)
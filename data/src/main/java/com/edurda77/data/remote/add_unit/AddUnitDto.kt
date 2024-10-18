package com.edurda77.data.remote.add_unit


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddUnitDto(
    @SerialName("name")
    val name: String,
    @SerialName("short")
    val short: String
)
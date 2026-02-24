package com.edurda77.data.remote.measurement

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeasurementUnitDto(
    @SerialName("abbreviation")
    val abbreviation: String,
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)
package com.edurda77.data.remote.newDtos.measurement


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MeasurementUnitCreateRequest(
    @SerialName("abbreviation")
    val abbreviation: String,
    @SerialName("name")
    val name: String
)
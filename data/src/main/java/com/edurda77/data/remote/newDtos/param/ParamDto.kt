package com.edurda77.data.remote.newDtos.param


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParamDto(
    @SerialName("classIcon")
    val classIcon: String,
    @SerialName("color")
    val color: String,
    @SerialName("id")
    val id: String,
    @SerialName("isHidden")
    val isHidden: Boolean,
    @SerialName("label")
    val label: String,
    @SerialName("measurementUnitDto")
    val measurementUnitDto: MeasurementUnitDto,
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: Double
)
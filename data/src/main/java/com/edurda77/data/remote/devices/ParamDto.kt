package com.edurda77.data.remote.devices


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParamDto(
    @SerialName("classIcon")
    val classIcon: String,
    @SerialName("color")
    val color: String,
    @SerialName("id")
    val id: Int,
    @SerialName("id_device")
    val idDevice: Int,
    @SerialName("id_unit")
    val idUnit: Int,
    @SerialName("isHidden")
    val isHidden: Int,
    @SerialName("label")
    val label: String,
    @SerialName("name")
    val name: String,
    @SerialName("value")
    val value: String
)
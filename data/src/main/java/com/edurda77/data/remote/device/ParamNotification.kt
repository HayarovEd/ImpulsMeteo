package com.edurda77.data.remote.device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ParamNotification(
    @SerialName("condition")
    val condition: String,
    @SerialName("id")
    val id: Int?,
    @SerialName("id_param")
    val idParam: Int,
    @SerialName("value")
    val value: Int
)
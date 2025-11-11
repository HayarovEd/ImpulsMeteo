package com.edurda77.data.remote.devices


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeleteDeviceParamResponse(
    @SerialName("devices_params_record_deleted")
    val devicesParamsRecordDeleted: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("status")
    val status: String
)
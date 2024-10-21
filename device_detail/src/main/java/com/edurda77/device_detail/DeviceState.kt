package com.edurda77.device_detail


import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.resources.uikit.UiText

data class DeviceState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val deviceId: Int = NEGATIVE_ID,
    val loggedUser: LoggedUser? = null,
    val device: SingleDevice? = null,
)

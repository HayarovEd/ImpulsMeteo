package com.edurda77.device_detail


import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.resources.uikit.UiText
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now

data class DeviceState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val deviceId: Int = NEGATIVE_ID,
    val loggedUser: LoggedUser? = null,
    val device: SingleDevice? = null,
    val fromDate: LocalDateTime = LocalDateTime.now(),
    val toDate: LocalDateTime = Clock.System.now()
        .minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault())
)

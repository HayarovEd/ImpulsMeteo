package com.edurda77.device_detail


import com.edurda77.domain.model.ElementHistory
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteo
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
    val units: List<UnitMeteo> = emptyList(),
    val groups: List<GroupDevices> = emptyList(),
    val toDate: LocalDateTime = LocalDateTime.now(),
    val fromDate: LocalDateTime = Clock.System.now()
        .minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val historyStates: List<List<ElementHistory>> = emptyList(),
    val isLoadingHistory: Boolean = false,
)


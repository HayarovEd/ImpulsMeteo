package com.edurda77.device_detail


import com.edurda77.domain.model.AuthUser
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.History
import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.model.MeasurementUnit
import com.edurda77.resources.uikit.UiText
import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.utils.now
import kotlin.time.Clock

data class DeviceState (
    val isLoading: Boolean = true,
    val device: Device? = null,
    val units: List<MeasurementUnit> = emptyList(),
    val groups: List<GroupDevice> = emptyList(),
    val toDate: LocalDateTime = LocalDateTime.now(),
    val fromDate: LocalDateTime = Clock.System.now()
        .minus(DateTimePeriod(months = 1), TimeZone.currentSystemDefault())
        .toLocalDateTime(TimeZone.currentSystemDefault()),
    val histories: Map<String, List<History>> = emptyMap(),
    val isLoadingHistory: Boolean = false,
    val authUser: AuthUser? = null,
    val selectedGroups: List<GroupDevice> = emptyList(),
)


sealed interface UiDeviceEvents {
    data object LoginNavigationEvent : UiDeviceEvents
    data object BackUpNavigationEvent : UiDeviceEvents
    data class OnError(val message: UiText) : UiDeviceEvents
}


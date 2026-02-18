package com.edurda77.device_detail


import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.ElementHistory
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.MeasurementUnit
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
    val historyStates: List<List<ElementHistory>> = emptyList(),
    val isLoadingHistory: Boolean = false,
    val authUser: AuthUser? = null,
)


sealed interface UiDeviceEvents {
    data object LoginNavigationEvent : UiDeviceEvents
    data class OnError(val message: UiText) : UiDeviceEvents
}


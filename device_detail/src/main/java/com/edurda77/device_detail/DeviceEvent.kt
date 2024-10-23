package com.edurda77.device_detail

import kotlinx.datetime.LocalDateTime


sealed class DeviceEvent {
    class OnSetFromDate(val dateTime: LocalDateTime) : DeviceEvent()
    class OnSetToDate(val dateTime: LocalDateTime) : DeviceEvent()
    class GetHistory(val limit: Int) : DeviceEvent()
}
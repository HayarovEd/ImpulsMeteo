package com.edurda77.device_detail

import kotlinx.datetime.LocalDateTime


sealed class DeviceEvent {
    class onSetFromDate(val dateTime: LocalDateTime) : DeviceEvent()
    class onSetToDate(val dateTime: LocalDateTime) : DeviceEvent()
}
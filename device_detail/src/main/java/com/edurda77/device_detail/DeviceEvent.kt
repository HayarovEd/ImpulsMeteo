package com.edurda77.device_detail

import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.SingleDevice
import kotlinx.datetime.LocalDateTime


sealed class DeviceEvent {
    class OnSetFromDate(val dateTime: LocalDateTime) : DeviceEvent()
    class OnSetToDate(val dateTime: LocalDateTime) : DeviceEvent()
    class GetHistory(val limit: Int) : DeviceEvent()
    class AddNewNotificationToList(
        val idParam: Int,
        val condition: String,
        val value: Int
    ) : DeviceEvent()
    class DeleteNotificationFromList(
        val index: Int,
    ) : DeviceEvent()

    class UpdateNotificationInList(
        val index: Int,
        val id: Int,
        val idParam: Int,
        val condition: String,
        val value: Int
    ) : DeviceEvent()

    data object ChangeStatusNotifications : DeviceEvent()
    data object UpdateNotifications : DeviceEvent()
    class UpdateSelectedGroups(val groupDevices: GroupDevices) : DeviceEvent()
    class UpdateDevice(val device: SingleDevice) : DeviceEvent()
    data object BackStartGroups : DeviceEvent()
}
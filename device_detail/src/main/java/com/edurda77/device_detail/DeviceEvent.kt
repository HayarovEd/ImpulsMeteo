package com.edurda77.device_detail

import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.Param
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
    class UpdateParam(val param: Param) : DeviceEvent()
    class UpdateDevice(val name: String, val key: String, val frequency: String) : DeviceEvent()
    data object BackStartGroups : DeviceEvent()
}
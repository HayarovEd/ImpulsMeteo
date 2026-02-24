package com.edurda77.device_detail

import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.NotificationParam
import com.edurda77.domain.model.newModels.Param
import kotlinx.datetime.LocalDateTime


sealed class DeviceEvent {
    class OnSetFromDate(val dateTime: LocalDateTime) : DeviceEvent()//
    class OnSetToDate(val dateTime: LocalDateTime) : DeviceEvent()//
    class GetHistory(val limit: Int) : DeviceEvent()//
    class UpdateNotifications(
        val notificationsParam: List<NotificationParam>,
        val value: Boolean,
    ) : DeviceEvent()//
    class UpdateSelectedGroups(val groupDevices: GroupDevice) : DeviceEvent()//
    class UpdateParam(val param: Param) : DeviceEvent()//
    class UpdateDevice(val name: String, val key: String, val frequency: String) : DeviceEvent()//
    data object DeleteDevice : DeviceEvent()
    data object BackStartGroups : DeviceEvent()//
    data object WorkWithFavorite : DeviceEvent()//
    data object ClearDeviceSensorData : DeviceEvent()//
}
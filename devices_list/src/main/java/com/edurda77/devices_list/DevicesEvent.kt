package com.edurda77.devices_list

import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.GroupDevicesOld

sealed class DevicesEvent {
    data object Refresh : DevicesEvent()
    data object Logoff : DevicesEvent()
    class OnSearch(val query: String) : DevicesEvent()
    class SelectGroup(val index: Int) : DevicesEvent()
    data object ShowSearchField : DevicesEvent()
    class UpdateSelectedGroups(val groupDevicesOld: GroupDevicesOld) : DevicesEvent()
    data object ClearSelectedGroups : DevicesEvent()
    class OnInsertDevice(
        val name: String,
        val key: String,
        val frequency: String,
        val groups: List<GroupDevicesOld>,
    ) : DevicesEvent()

    data object OnCloseWebSocket : DevicesEvent()
    class WorkWithFavorite(val deviceOld: DeviceOld) : DevicesEvent()
    class OnDeleteDevice(val deviceOld: DeviceOld) : DevicesEvent()
    data object UpdateApp:DevicesEvent()
    data object SortDevicesByStatus:DevicesEvent()
}
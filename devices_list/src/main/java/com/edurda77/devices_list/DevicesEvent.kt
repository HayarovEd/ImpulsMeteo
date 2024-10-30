package com.edurda77.devices_list

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices

sealed class DevicesEvent {
    data object Refresh : DevicesEvent()
    data object Logoff : DevicesEvent()
    class OnSearch(val query: String) : DevicesEvent()
    class SelectGroup(val index: Int) : DevicesEvent()
    data object ShowSearchField : DevicesEvent()
    class UpdateSelectedGroups(val groupDevices: GroupDevices) : DevicesEvent()
    data object ClearSelectedGroups : DevicesEvent()
    class OnInsertDevice(
        val name: String,
        val key: String,
        val frequency: String,
        val groups: List<GroupDevices>,
    ) : DevicesEvent()

    data object OnCloseWebSocket : DevicesEvent()
    class WorkWithFavorite(val device: Device) : DevicesEvent()
    class OnDeleteDevice(val device: Device) : DevicesEvent()
}
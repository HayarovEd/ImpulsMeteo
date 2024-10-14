package com.edurda77.devices_list

sealed class DevicesEvent {
    data object Refresh: DevicesEvent()
    data object Logoff: DevicesEvent()
    class OnSearch(val query: String) : DevicesEvent()
    class SelectGroup(val name: String): DevicesEvent()
}
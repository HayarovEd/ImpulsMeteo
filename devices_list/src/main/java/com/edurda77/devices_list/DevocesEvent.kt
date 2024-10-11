package com.edurda77.devices_list

sealed class DevocesEvent {
    data object Refresh: DevocesEvent()
    data object Logoff: DevocesEvent()
    class OnSearch(val query: String) : DevocesEvent()
    class SelectGroup(val name: String): DevocesEvent()
}
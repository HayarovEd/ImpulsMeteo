package com.edurda77.devices_list


import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.resources.uikit.UiText

data class DevicesState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val query: String = "",
    val selectedGroup: String = "",
    val loggedUser: LoggedUser? = null,
    val devices: Map<GroupDevices, List<Device>> = emptyMap(),
)

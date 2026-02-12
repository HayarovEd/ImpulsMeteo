package com.edurda77.devices_list


import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.utils.convertToMapGroupedDevices2
import com.edurda77.domain.utils.filterGroupedDevices2
import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.resources.uikit.UiText

data class DevicesState(
    val isLoading: Boolean = false,
    val query: String = "",
    val numberSelectedGroup: Int = 0,
    val devices: Map<GroupDevicesOld, List<DeviceOld>> = emptyMap(),
    val isShowSearch: Boolean = false,
    val selectedGroups: List<GroupDevice> = emptyList(),
    val groups: List<GroupDevice> = emptyList(),
    val enableUpdate: Boolean = false,
    val isUpdating: Boolean = false,
    val percentUpdate: Int = 0,
    val release: LastVersionApp? = null,
    val isSorted: Boolean = false,
    val authUser: AuthUser? = null,
    val updatingDeviceIds: List<String> = emptyList()
) {
    private val groupedDevices = authUser?.let {userData->
        convertToMapGroupedDevices2(
            devices = userData.devices,
            favorites = userData.favorites
        )
    }?: emptyMap()
    val filteredDevices = filterGroupedDevices2(
        devices = groupedDevices,
        query = query,
        isSorted = isSorted
    )
}

sealed interface UiDevicesEvents {
    data object LoginNavigationEvent : UiDevicesEvents
    data class OnError(val message: UiText) : UiDevicesEvents
}

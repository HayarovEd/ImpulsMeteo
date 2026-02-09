package com.edurda77.devices_list


import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.utils.convertToMapGroupedDevices2
import com.edurda77.domain.utils.filterGroupedDevices2
import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.resources.uikit.UiText

data class DevicesState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val query: String = "",
    val numberSelectedGroup: Int = 0,
    val loggedUser: LoggedUser? = null,
    val devices: Map<GroupDevicesOld, List<DeviceOld>> = emptyMap(),
    val isShowSearch: Boolean = false,
    val selectedGroups: List<GroupDevicesOld> = emptyList(),
    val groups: List<GroupDevicesOld> = emptyList(),
    val enableUpdate: Boolean = false,
    val isUpdating: Boolean = false,
    val percentUpdate: Int = 0,
    val release: LastVersionApp? = null,
    val isSorted: Boolean = false,
    //
    val authUser: AuthUser? = null,
) {
   /* val nonHiddenDevices = authUser?.devices.mapValues { (_, devices) ->
        devices.map { device ->
            device.copy(params = device.params.filter { !it.isHidden })
        }
    }*/
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

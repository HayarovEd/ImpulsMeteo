package com.edurda77.devices_list


import android.util.Log
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.LoggedUser
import com.edurda77.download_install.model.LastVersionApp
import com.edurda77.resources.uikit.UiText

data class DevicesState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val query: String = "",
    val numberSelectedGroup: Int = 0,
    val loggedUser: LoggedUser? = null,
    val devices: Map<GroupDevices, List<Device>> = emptyMap(),
    val isShowSearch: Boolean = false,
    val selectedGroups: List<GroupDevices> = emptyList(),
    val groups: List<GroupDevices> = emptyList(),
    val enableUpdate: Boolean = false,
    val isUpdating: Boolean = false,
    val percentUpdate: Int = 0,
    val release: LastVersionApp? = null,
    val isSorted: Boolean = true,
) {
    val nonHiddenDevices = devices.mapValues { (_, devices) ->
        devices.map { device ->
            if (device.name=="Восточная"){
                device.params.forEach {
                    Log.d("TEST METEO APP", "unfiltered param $it")
                }
            }
            device.copy(params = device.params.filter { !it.isHidden })
        }
    }
}

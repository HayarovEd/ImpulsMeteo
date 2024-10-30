package com.edurda77.domain.utils

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices


fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
    return email.matches(emailRegex)
}


fun convertToMapGroupedDevices(
    devices: List<Device>,
): Map<GroupDevices, List<Device>> {
    val groupedDevices = mutableMapOf<GroupDevices, List<Device>>()
    val groups = mutableListOf<GroupDevices>()
    val favoriteDevices = mutableListOf<Device>()
    devices.forEach { device ->
        if (device.isFavorite) {
            favoriteDevices.add(device)
        }
        groups.addAll(device.groups.filterNot { it in groups })
    }
    if (favoriteDevices.isNotEmpty()) {
        groupedDevices[GroupDevices(
            id = FAVORITE_ID_GROUP,
            name = FAVORITE
        )] = favoriteDevices
    }
    groups
        .sortedBy { it.id }
        .forEach { group ->
            val enteredDevices = devices.filter { it.groups.contains(group) }
            groupedDevices[group] = enteredDevices
        }
    return groupedDevices
}

fun filterGroupedDevices(
    devices: Map<GroupDevices, List<Device>>,
    query: String,
): Map<GroupDevices, List<Device>> {
    return devices
        .mapValues { (_, current) ->
            current.filter {
                it.name
                    .contains(
                        other = query,
                        ignoreCase = true
                    )
            }
        }
}

fun updateDevices(
    devices: Map<GroupDevices, List<Device>>,
    newDevice: Device
): Map<GroupDevices, List<Device>> {

    return devices.mapValues { (_, deviceList) ->
        deviceList.map { device ->
            if (device.id == newDevice.id) {
                val updatedDevice = newDevice.copy(isFavorite = device.isFavorite)
                updatedDevice
            } else device
        }
    }
}


package com.edurda77.domain.utils

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.SingleDevice


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

fun updateDevice(
    device: SingleDevice,
    newDevice: Device
): SingleDevice {
    return device.copy(
        name = newDevice.name,
        key = newDevice.key,
        status = newDevice.status,
        video = newDevice.video,
        updatedAt = newDevice.updatedAt,
        groups = newDevice.groups,
        params = newDevice.params
    )
}

@Suppress("DefaultLocale")
fun formatted(
    value: Double,
    unit: String
): String {
    val formattedValue = if (value > 100.0) {
        ((value * 100).toInt() / 100.0).toString()
    } else {
        String.format("%.2f", value)
    }
    /* val formatter = NumberFormat.getNumberInstance(Locale.getDefault()).apply {
         val fractionDigits = when {
             value > 100 -> 0
             else -> 2
             //else -> 3
         }
         maximumFractionDigits = fractionDigits
         minimumFractionDigits = 0
     }*/
    return "${formattedValue}$unit"
}

fun calculateInterval(size: Int): Int {
    return when {
        size < 6 -> 1
        else -> {
            (size - 1) / 5 + 1
        }
    }
}




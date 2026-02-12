package com.edurda77.domain.utils

import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.Favorite
import com.edurda77.domain.model.newModels.GroupDevice


fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
    return email.matches(emailRegex)
}


fun convertToMapGroupedDevices2(
    devices: List<Device>,
    favorites: List<Favorite>,
): Map<GroupDevice, List<Device>> {
    val groupedDevices = mutableMapOf<GroupDevice, List<Device>>()
    val groups = mutableListOf<GroupDevice>()
    val favoriteDevice = mutableListOf<Device>()
    val nonHiddenParamsDevices = devices.map {
        it.copy(
            params = it.params.filter { pr-> !pr.isHidden }
        )
    }
    nonHiddenParamsDevices.forEach { device ->
        if (device.id in favorites.map { it.deviceId }) {
            favoriteDevice.add(device)
        }
        groups.addAll(device.groups.filterNot { it in groups })
    }
    if (favoriteDevice.isNotEmpty()) {
        groupedDevices[GroupDevice(
            id = FAVORITE_ID_GROUP_STRING,
            name = FAVORITE
        )] = favoriteDevice
    }
    groups
        .sortedBy { it.id }
        .forEach { group ->
            val enteredDevices = devices.filter { it.groups.contains(group) }
            groupedDevices[group] = enteredDevices
        }
    return groupedDevices
}

fun filterGroupedDevices2(
    devices: Map<GroupDevice, List<Device>>,
    query: String,
    isSorted: Boolean,
): Map<GroupDevice, List<Device>> {
    return devices
        .mapValues { (_, current) ->
            val searched = current.filter {
                it.name
                    .contains(
                        other = query,
                        ignoreCase = true
                    )
            }
            if (isSorted) {
                searched.sortedByDescending { it.status }
            } else searched
        }
}

fun updateDevice(
    device: SingleDevice,
    newDeviceOld: DeviceOld
): SingleDevice {
    return device.copy(
        name = newDeviceOld.name,
        key = newDeviceOld.key,
        status = newDeviceOld.status,
        video = newDeviceOld.video,
        updatedAt = newDeviceOld.updatedAt,
        groups = newDeviceOld.groups,
        params = newDeviceOld.params
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
    return formattedValue
}

fun calculateInterval(size: Int): Int {
    return when {
        size < 6 -> 1
        else -> {
            (size - 1) / 5 + 1
        }
    }
}




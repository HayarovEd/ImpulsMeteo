package com.edurda77.domain.utils

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices


fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,3})+$")
    return email.matches(emailRegex)
}
/*

@OptIn(ExperimentalEncodingApi::class)
fun decodeToken(jwt: String): String {
    val parts = jwt.split(".")
    return try {
        val header = String(Base64.decode(parts[0]))
        val payload = String(Base64.decode(parts[1]))
        //"$header"
        //"$payload"

        println("payload $payload")
        payload
    } catch (e: Exception) {
        "Error parsing JWT: $e"
    }
}*/


fun convertToMapGroupedDevices(
    groups: List<GroupDevices>,
    devices: List<Device>
): Map<GroupDevices, List<Device>> {
    val groupedDevices = mutableMapOf<GroupDevices, List<Device>>()
    groups.forEach { group->
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


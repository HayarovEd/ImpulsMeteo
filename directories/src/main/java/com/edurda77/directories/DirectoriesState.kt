package com.edurda77.directories

import com.edurda77.domain.model.AuthUser
import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.model.MeasurementUnit
import com.edurda77.resources.uikit.UiText

data class DirectoriesState(
    val isLoading: Boolean = true,
    val directoriesType: DirectoriesType = DirectoriesType.GROUPS,
    val groups: List<GroupDevice> = emptyList(),
    val units: List<MeasurementUnit> = emptyList(),
    val query: String = "",

    //
    val authUser: AuthUser? = null,
) {
    val filteredGtoups = groups.filter {
        it.name.contains(query, ignoreCase = true)
    }

    val filteredMeasurementUnits = units.filter {
        it.name.contains(query, ignoreCase = true)
    }
}


enum class DirectoriesType {
    GROUPS,
    UNITS
}

sealed interface UiDirectoriesEvents {
    data object LoginNavigationEvent : UiDirectoriesEvents
    data class OnError(val message: UiText) : UiDirectoriesEvents
}
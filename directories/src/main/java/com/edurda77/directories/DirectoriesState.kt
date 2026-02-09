package com.edurda77.directories

import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.model.UnitMeteo
import com.edurda77.resources.uikit.UiText

data class DirectoriesState(
    val message: UiText? = null,
    val isLoading: Boolean = true,
    val token: String = "",
    val loggedUser: LoggedUser? = null,
    val directoriesType: DirectoriesType = DirectoriesType.GROUPS,
    val groups: List<GroupDevicesOld> = emptyList(),
    val units: List<UnitMeteo> = emptyList(),
    val query: String = "",
)


enum class DirectoriesType {
    GROUPS,
    UNITS
}
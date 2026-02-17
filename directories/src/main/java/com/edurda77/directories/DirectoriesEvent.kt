package com.edurda77.directories

import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.MeasurementUnit

sealed class DirectoriesEvent {
    data object Refresh : DirectoriesEvent()//
    data object Logoff : DirectoriesEvent()//
    class SwitchDirectoriesType(val directoriesType: DirectoriesType) : DirectoriesEvent()//
    class AddDevicesGroup(val name: String) : DirectoriesEvent()
    class AddUnit(
        val name: String,
        val short: String
    ) : DirectoriesEvent()

    class DeleteDevicesGroup(val id: String) : DirectoriesEvent()
    class DeleteUnit(val id: String) : DirectoriesEvent()
    class UpdateDevicesGroup(
        val groupDevice: GroupDevice
    ) : DirectoriesEvent()

    class UpdateUnit(
        val measurementUnit: MeasurementUnit,
    ) : DirectoriesEvent()

    class OnSearch(val query:String) : DirectoriesEvent()//
}
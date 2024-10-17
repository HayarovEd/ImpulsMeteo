package com.edurda77.directories

sealed class DirectoriesEvent {
    data object Refresh : DirectoriesEvent()
    data object Logoff : DirectoriesEvent()
    class SwitshDirectoriesType(val directoriesType: DirectoriesType) : DirectoriesEvent()
}
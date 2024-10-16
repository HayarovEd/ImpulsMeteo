package com.edurda77.users_list


sealed class UsersEvent {
    data object Refresh : UsersEvent()
    data object Logoff : UsersEvent()
    /* class UpdateSelectedGroups(val groupDevices: GroupDevices) : UsersEvent()
     data object ClearSelectedGroups : UsersEvent()*/
}
package com.edurda77.users_list.model

import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.PermissionUser

data class UserUiOld(
    val email: String,
    val id: Int,
    val name: String,
    val devices: List<DeviceUser>,
    val permissions: List<PermissionUser>,
    val isExpanded: Boolean = false
)

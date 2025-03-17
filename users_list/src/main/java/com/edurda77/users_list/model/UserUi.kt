package com.edurda77.users_list.model

import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser

data class UserUi(
    val email: String,
    val id: Int,
    val name: String,
    val devices: List<DeviceUser>,
    val permissions: List<PermissionUser>,
    val isExpanded: Boolean = false
)

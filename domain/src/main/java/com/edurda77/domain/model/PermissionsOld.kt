package com.edurda77.domain.model

import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.PermissionUser

data class PermissionsOld(
    val permissions: List<PermissionUser>,
    val devicesPermission: List<DeviceUser>
)

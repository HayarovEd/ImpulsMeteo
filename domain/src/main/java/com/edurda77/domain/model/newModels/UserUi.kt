package com.edurda77.domain.model.newModels

data class UserUi (
    val devices: List<DeviceUser>,
    val email: String,
    val id: String,
    val isEnabled: Boolean,
    val name: String,
    val password: String,
    val permissions: List<PermissionUser>,
    val isExpanded: Boolean = false
)
package com.edurda77.domain.model


data class User(
    val email: String,
    val id: Int,
    val name: String,
    val devices: List<DeviceUser>,
    val permissions: List<PermissionUser>
)

package com.edurda77.domain.model

data class SingleDevice(
    val id: Int,
    val name: String,
    val key: String,
    val status: Boolean,
    val video: String?,
    val updatedAt: String,
    val frequency: Int,
    val groups: List<GroupDevices>,
    val params: List<Param>,
    val notifications: Notifications
)

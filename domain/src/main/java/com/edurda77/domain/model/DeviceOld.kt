package com.edurda77.domain.model

data class DeviceOld(
    val id: Int,
    val name: String,
    val key: String,
    val status: Boolean,
    val video: String?,
    val updatedAt: String,
    val groups: List<GroupDevicesOld>,
    val params: List<ParamOld>,
    val isFavorite: Boolean = false,
    val statusNotifications: Boolean,
)

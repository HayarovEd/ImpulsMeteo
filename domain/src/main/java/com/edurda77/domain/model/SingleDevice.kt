package com.edurda77.domain.model

data class SingleDevice(
    val id: Int,
    val name: String,
    val key: String,
    val status: Boolean,
    val host: String,
    val port: Int,
    val video: String?,
    val updatedAt: String?,
    val frequency: Int,
    val groups: List<GroupDevicesOld>,
    val params: List<Param>,
    val notificationsOld: NotificationsOld,
    val isFavorite: Boolean = false
)

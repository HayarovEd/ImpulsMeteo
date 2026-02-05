package com.edurda77.domain.model.newModels


data class NotificationDevice(
    val deviceId: String,
    val id: String,
    val notificationsDevice: List<NotificationsDevice>,
    val userId: String,
    val value: Boolean
)

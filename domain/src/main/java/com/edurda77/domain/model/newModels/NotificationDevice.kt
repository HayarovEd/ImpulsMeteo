package com.edurda77.domain.model.newModels


data class NotificationDevice(
    val deviceId: String,
    val id: String,
    val notificationParam: List<NotificationParam>,
    val userId: String,
    val value: Boolean
)

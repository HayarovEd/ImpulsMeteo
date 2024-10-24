package com.edurda77.domain.model

data class Notifications(
    val deviceStatus: Boolean,
    val notifications: List<NotificationDevice>
)

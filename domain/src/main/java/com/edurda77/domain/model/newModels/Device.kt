package com.edurda77.domain.model.newModels



data class Device (
    val host: String,
    val id: String,
    val key: String,
    val name: String,
    val notificationDevice: NotificationDevice?,
    val port: Int,
    val status: Boolean,
    val updateRate: Int,
    val updatedDate: String,
    val videoUrl: String,
    val groups: List<GroupDevice>,
    val params: List<Param>,
)
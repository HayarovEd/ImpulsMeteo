package com.edurda77.domain.model.newModels


data class NotificationParam(
    val condition: String,
    val id: String,
    val isSend: Boolean,
    val paramId: String,
    val userId: String,
    val value: Int
)

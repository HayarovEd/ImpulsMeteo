package com.edurda77.domain.model

data class NotificationDevice(
    val condition: String,
    val id: Int? = null,
    val idParam: Int,
    val value: Int
)

package com.edurda77.domain.model

data class MessageWebSocketStart(
    val activityTimeout: Int,
    val socketId: String,
    val event: String
)
package com.edurda77.domain.model.newModels

data class Favorite(
    val createdAt: String,
    val deviceId: String,
    val id: String,
    val updateAt: String,
    val userId: String,
    val isUpdating: Boolean = false,
)

package com.edurda77.domain.model.newModels

import kotlinx.datetime.LocalDateTime


data class User(
    val createdAt: String,
    val devices: List<Device>,
    val email: String,
    val favorites: List<Favorite>,
    val id: String,
    val isEnabled: Boolean,
    val name: String,
    val password: String,
    val permissions: List<Permission>,
    val updateAt: LocalDateTime
)

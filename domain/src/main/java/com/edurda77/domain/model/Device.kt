package com.edurda77.domain.model

data class Device(
    val id: Int,
    val name: String,
    val key: String,
    val status: Boolean,
    val video: String?,
    val updatedAt: String,
    val groups: List<GroupDevices>,
    val params: List<Param>,
)

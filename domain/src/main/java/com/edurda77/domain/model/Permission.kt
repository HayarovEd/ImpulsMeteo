package com.edurda77.domain.model

data class Permission(
    val description: String,
    val displayName: String,
    val id: String,
    val name: String,
    val parent: Int
)

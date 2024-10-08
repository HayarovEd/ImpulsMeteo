package com.edurda77.domain.model

data class User(
    val id: Int,
    val name: String,
    val email: String,
    val permissions: List<Int>
)

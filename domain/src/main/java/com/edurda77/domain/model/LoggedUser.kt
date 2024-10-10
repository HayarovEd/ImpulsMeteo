package com.edurda77.domain.model

data class LoggedUser(
    val id: Int,
    val name: String,
    val email: String,
    val permissions: List<Int>
)

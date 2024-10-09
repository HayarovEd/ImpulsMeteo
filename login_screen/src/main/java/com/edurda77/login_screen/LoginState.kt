package com.edurda77.login_screen


data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
)

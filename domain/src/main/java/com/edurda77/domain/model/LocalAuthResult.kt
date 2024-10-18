package com.edurda77.domain.model

sealed interface LocalAuthResult {
    data object LocalNotSession: LocalAuthResult
    data object  LocalSession: LocalAuthResult
}
package com.edurda77.domain.model.newModels

sealed interface LocalAuthResult {
    data object LocalNotSession: LocalAuthResult
    data object  LocalSession: LocalAuthResult
}
package com.edurda77.domain.model

sealed interface HistoryState {
    data object Empty : HistoryState
    class Success(val history: List<ElementHistory>) : HistoryState
}
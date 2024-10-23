package com.edurda77.domain.model

data class Param(
    val id: Int,
    val idUnit: Int,
    val name: String,
    val label: String,
    val value: Double,
    val classIcon: String,
    val isHidden: Boolean,
)

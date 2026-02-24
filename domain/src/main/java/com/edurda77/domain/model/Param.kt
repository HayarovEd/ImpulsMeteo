package com.edurda77.domain.model


data class Param (
    val classIcon: String,
    val color: String,
    val id: String,
    val isHidden: Boolean,
    val label: String,
    val measurementUnit: MeasurementUnit,
    val name: String,
    val value: Double
)
package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface MeasurementUnitRepository {
    suspend fun getUnits(accessToken: String): ResultWork<List<MeasurementUnit>, DataError>
    suspend fun insertUnit(
        accessToken: String,
        name: String,
        abbreviation: String
    ): ResultWork<MeasurementUnit, DataError>

    suspend fun updateUnit(
        accessToken: String,
        measurementUnit: MeasurementUnit
    ): ResultWork<MeasurementUnit, DataError>

    suspend fun deleteUnit(accessToken: String, id: String): ResultWork<Unit, DataError>
}
package com.edurda77.domain.usecase

import com.edurda77.domain.model.MeasurementUnit
import com.edurda77.domain.repository.MeasurementUnitRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UnitsUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<List<MeasurementUnit>, DataError> {
        return tokenManager.validateFactory(
            data = {
                measurementUnitRepository.getUnits(it)
            },
        )
    }
}
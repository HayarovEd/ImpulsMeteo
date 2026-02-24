package com.edurda77.domain.usecase

import com.edurda77.domain.model.MeasurementUnit
import com.edurda77.domain.repository.MeasurementUnitRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateUnitUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        measurementUnit: MeasurementUnit,
    ): ResultWork<MeasurementUnit, DataError> {
        if (measurementUnit.name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        return tokenManager.validateFactory(
            data = {
                measurementUnitRepository.updateUnit(
                    accessToken = it,
                    measurementUnit = measurementUnit
                )
            }
        )
    }
}
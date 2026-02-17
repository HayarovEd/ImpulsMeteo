package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.repository.MeasurementUnitRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class AddUnitUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        name: String,
        short: String,
    ): ResultWork<MeasurementUnit, DataError> {
        if (name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        return tokenManager.validateFactory(
            data = {
                measurementUnitRepository.insertUnit(
                    accessToken = it,
                    name = name,
                    abbreviation = short
                )
            }
        )
    }
}
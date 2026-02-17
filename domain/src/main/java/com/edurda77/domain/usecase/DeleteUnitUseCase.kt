package com.edurda77.domain.usecase

import com.edurda77.domain.repository.MeasurementUnitRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteUnitUseCase(
    private val measurementUnitRepository: MeasurementUnitRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        id: String
    ): ResultWork<Unit, DataError> {
        return tokenManager.validateFactory(
            data = {
                measurementUnitRepository.deleteUnit(
                    accessToken = it,
                    id = id
                )
            }
        )
    }
}
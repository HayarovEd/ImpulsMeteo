package com.edurda77.domain.usecase

import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UnitsUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<UnitMeteo>, DataError> {
        return oldRemoteRepository.getUnits(
            token = token,
        )
    }
}
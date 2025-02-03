package com.edurda77.domain.usecase

import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UnitsUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<UnitMeteo>, DataError> {
        return remoteRepository.getUnits(
            token = token,
        )
    }
}
package com.edurda77.domain.usecase

import com.edurda77.domain.repository.ParamsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

class ClearHistoryDeviceUseCase(
    private val paramsRepository: ParamsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        ids: List<String>,
    ): ResultWork<Unit, DataError> {
        return tokenManager.validateFactory(
            data = {
                paramsRepository.clearHistory(
                    accessToken = it,
                    ids = ids
                )
            },
        )
    }
}
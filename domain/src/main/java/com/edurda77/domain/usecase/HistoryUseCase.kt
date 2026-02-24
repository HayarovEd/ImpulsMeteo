package com.edurda77.domain.usecase

import com.edurda77.domain.model.History
import com.edurda77.domain.repository.ParamsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class HistoryUseCase(
    private val paramsRepository: ParamsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        id: String,
        fromDate: String,
        toDate: String,
        limit: Int,
    ): ResultWork<Map<String, List<History>>, DataError> {
        return tokenManager.validateFactory(
            data = {
                paramsRepository.getHistoryDeviceById(
                    accessToken = it,
                    deviceId = id,
                    fromDate = fromDate,
                    toDate = toDate,
                    limit = limit
                )
            },
        )
    }
}
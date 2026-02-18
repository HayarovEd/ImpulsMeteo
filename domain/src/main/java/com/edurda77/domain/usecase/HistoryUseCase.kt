package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.ElementHistory
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class HistoryUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        fromDate: String,
        toDate: String,
        limit: Int,
    ): ResultWork<List<List<ElementHistory>>, DataError> {

        return when (val result = oldRemoteRepository.getHistoryDeviceById(
            id = id,
            fromDate = fromDate,
            limit = limit,
            toDate = toDate,
            token = token,
        )) {
            is ResultWork.Error -> {
                ResultWork.Error(result.error)
            }

            is ResultWork.Success -> {
                ResultWork.Success(result.data)
            }
        }
    }
}
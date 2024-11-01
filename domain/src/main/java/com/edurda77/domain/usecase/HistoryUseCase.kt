package com.edurda77.domain.usecase

import com.edurda77.domain.model.HistoryState
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import javax.inject.Inject

class HistoryUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        fromDate: String,
        toDate: String,
        limit: Int,
    ): ResultWork<List<HistoryState>, DataError> {

        return when (val result = remoteRepository.getHistoryDeviceById(
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
                val historyStates = mutableListOf<HistoryState>()
                result.data.map { history ->
                    if (history.isEmpty()) {
                        historyStates.add(HistoryState.Empty)
                    } else {
                        historyStates.add(HistoryState.Success(history))
                    }
                }
                ResultWork.Success(historyStates)
            }
        }
    }
}
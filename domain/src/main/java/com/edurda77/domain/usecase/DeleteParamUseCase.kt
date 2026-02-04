package com.edurda77.domain.usecase

import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteParamUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int
    ): ResultWork<Boolean, DataError> {
        return when (val result = oldRemoteRepository.deleteParam(
            token = token,
            id = id
        )) {
            is ResultWork.Error-> {
                ResultWork.Error(result.error)
            }
            is ResultWork.Success -> {
                ResultWork.Success(result.data)
            }
        }
    }
}
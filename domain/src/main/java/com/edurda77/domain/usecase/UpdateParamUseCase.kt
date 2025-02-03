package com.edurda77.domain.usecase


import com.edurda77.domain.model.Param
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateParamUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        param: Param
    ): ResultWork<Unit, DataError> {
        return remoteRepository.updateParam(
            token = token,
            param = param
        )
    }
}
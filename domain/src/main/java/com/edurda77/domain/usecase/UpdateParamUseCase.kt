package com.edurda77.domain.usecase


import com.edurda77.domain.model.ParamOld
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateParamUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        param: ParamOld
    ): ResultWork<Unit, DataError> {
        return oldRemoteRepository.updateParam(
            token = token,
            param = param
        )
    }
}
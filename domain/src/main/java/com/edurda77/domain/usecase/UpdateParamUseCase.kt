package com.edurda77.domain.usecase


import com.edurda77.domain.model.newModels.Param
import com.edurda77.domain.repository.ParamsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateParamUseCase(
    private val paramsRepository: ParamsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        param: Param
    ): ResultWork<Param, DataError> {
        return tokenManager.validateFactory(
            data = {
                paramsRepository.updateParam(
                    accessToken = it,
                    param = param
                )
            },
        )
    }
}
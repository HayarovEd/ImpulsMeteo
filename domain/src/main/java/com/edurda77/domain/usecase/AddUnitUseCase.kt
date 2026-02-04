package com.edurda77.domain.usecase

import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class AddUnitUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        name: String,
        short: String,
    ): ResultWork<Unit, DataError> {
        if (name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)

        return oldRemoteRepository.addUnit(
            name = name,
            token = token,
            short = short
        )
    }
}
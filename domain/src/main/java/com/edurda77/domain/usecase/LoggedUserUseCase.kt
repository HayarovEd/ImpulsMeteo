package com.edurda77.domain.usecase

import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class LoggedUserUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
    ): ResultWork<LoggedUser, DataError> {
        return oldRemoteRepository.authorizedUser(
            token = token
        )
    }
}
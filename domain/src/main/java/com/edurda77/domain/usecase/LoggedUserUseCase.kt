package com.edurda77.domain.usecase

import com.edurda77.domain.model.AuthUser
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class LoggedUserUseCase(
    private val remoteRepository: RemoteRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<AuthUser, DataError> {
        return tokenManager.validateFactory(
            data = {
                remoteRepository.loadAuthUserData(it)
            },
        )
    }
}
package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.AuthUser
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class LoggedUserUseCase(
    private val remoteRepository: RemoteRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<AuthUser, DataError> {
        /*return oldRemoteRepository.authorizedUser(
            token = token
        )*/
        return tokenManager.validateFactory(
            data = {
                remoteRepository.loadAuthUserData(it)
            },
        )
    }
}
package com.edurda77.domain.usecase

import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class LogOffUseCase(
    private val dataStoreRepository: DataStoreRepository,
    private val remoteRepository: RemoteRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<Unit, DataError> {
        dataStoreRepository.deleteTokens()
        return tokenManager.validateFactory(
            data = {
                remoteRepository.logout(it)
            }
        )
    }
}
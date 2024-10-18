package com.edurda77.domain.usecase

import com.edurda77.domain.model.LoggedUser
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import javax.inject.Inject

class LoggedUserUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
    ): ResultWork<LoggedUser, DataError> {
        return remoteRepository.authorizedUser(
            token = token
        )
    }
}
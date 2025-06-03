package com.edurda77.domain.usecase

import com.edurda77.domain.model.User
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UsersUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<User>, DataError> =
        remoteRepository.getUsers(token)
}
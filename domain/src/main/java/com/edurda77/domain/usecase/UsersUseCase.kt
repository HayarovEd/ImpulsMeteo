package com.edurda77.domain.usecase

import com.edurda77.domain.model.User
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UsersUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<User>, DataError> =
        oldRemoteRepository.getUsers(token)
}
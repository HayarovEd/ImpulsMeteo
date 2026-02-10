package com.edurda77.domain.usecase

import com.edurda77.domain.model.UserOld
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UsersUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<UserOld>, DataError> =
        oldRemoteRepository.getUsers(token)
}
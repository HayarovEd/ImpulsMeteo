package com.edurda77.domain.usecase

import com.edurda77.domain.repository.UsersRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteUserUseCase(
    private val usersRepository: UsersRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        userId: String
    ): ResultWork<Unit, DataError> {
        return tokenManager.validateFactory(
            data = {
                usersRepository.deleteUser(
                    accessToken = it,
                    userId = userId
                )
            }
        )
    }
}
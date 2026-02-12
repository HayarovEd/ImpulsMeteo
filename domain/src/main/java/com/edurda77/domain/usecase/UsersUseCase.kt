package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.repository.UsersRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UsersUseCase(
    private val usersRepository: UsersRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<List<User>, DataError> {
        return  tokenManager.validateFactory(
            data = {
                usersRepository.getUsers(it)
            },
        )
    }
}
package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.model.newModels.UserUi
import com.edurda77.domain.repository.UsersRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.isValidEmail


class UpdateUserUseCase(
    private val usersRepository: UsersRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(
        userUi: UserUi
    ): ResultWork<User, DataError> {
        if (userUi.name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        if (userUi.email.isBlank()) {
            return ResultWork.Error(DataError.EmailError.EMAIL_BLANK)
        } else if (!isValidEmail(userUi.email)) {
            return ResultWork.Error(DataError.EmailError.EMAIL_NOT_VALID)
        }
        return tokenManager.validateFactory(
            data = {
                usersRepository.updateUser(
                    accessToken = it,
                    userUi = userUi
                )
            },
        )
    }
}
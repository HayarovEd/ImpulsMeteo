package com.edurda77.domain.usecase

import com.edurda77.domain.model.Token
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.isValidEmail


class LoginUseCase(
    private val dataStoreRepository: DataStoreRepository,
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): ResultWork<Token, DataError> {

        if (email.isBlank()) {
            return ResultWork.Error(DataError.EmailError.EMAIL_BLANK)
        } else if (!isValidEmail(email)) {
            return ResultWork.Error(DataError.EmailError.EMAIL_NOT_VALID)
        }
        if (password.isBlank()) return ResultWork.Error(DataError.PasswordError.PASSWORD_BLANK)

        return when (val result = remoteRepository.authorization(
            email = email,
            password = password
        )) {
            is ResultWork.Error -> {
                ResultWork.Error(result.error)
            }

            is ResultWork.Success -> {
                dataStoreRepository.saveTokens(result.data)
                ResultWork.Success(result.data)
            }
        }

    }
}
package com.edurda77.domain.usecase

import com.edurda77.domain.model.Auth
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.isValidEmail
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): ResultWork<Auth, DataError> {

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
                if (result.data.id == NEGATIVE_ID) {
                    ResultWork.Error(DataError.NameError.ID_ERROR)
                } else {
                    dataStoreRepository.setAuthorization(result.data)
                    ResultWork.Success(result.data)
                }

            }
        }

    }
}
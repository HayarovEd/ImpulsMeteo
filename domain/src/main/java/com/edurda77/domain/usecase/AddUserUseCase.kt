package com.edurda77.domain.usecase

import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.isValidEmail
import javax.inject.Inject

class AddUserUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        devices: List<String>,
        permissions: List<String>,
        email: String,
        name: String,
        password: String,
    ): ResultWork<Unit, DataError> {
        if (name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        if (email.isBlank()) {
            return ResultWork.Error(DataError.EmailError.EMAIL_BLANK)
        } else if (!isValidEmail(email)) {
            return ResultWork.Error(DataError.EmailError.EMAIL_NOT_VALID)
        }
        if (password.isBlank()) return ResultWork.Error(DataError.PasswordError.PASSWORD_BLANK)

        return remoteRepository.addUser(
            devices = devices,
            permissions = permissions,
            name = name,
            token = token,
            email = email,
            password = password
        )
    }
}
package com.edurda77.domain.usecase

import com.edurda77.domain.model.DeviceUser
import com.edurda77.domain.model.PermissionUser
import com.edurda77.domain.model.User
import com.edurda77.domain.repository.UsersRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.isValidEmail


class AddUserUseCase(
    private val usersRepository: UsersRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        devices: List<DeviceUser>,
        permissions: List<PermissionUser>,
        email: String,
        name: String,
        password: String,
    ): ResultWork<User, DataError> {
        if (name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        if (email.isBlank()) {
            return ResultWork.Error(DataError.EmailError.EMAIL_BLANK)
        } else if (!isValidEmail(email)) {
            return ResultWork.Error(DataError.EmailError.EMAIL_NOT_VALID)
        }
        if (password.isBlank()) return ResultWork.Error(DataError.PasswordError.PASSWORD_BLANK)
        return tokenManager.validateFactory(
            data = {
                usersRepository.insertUser(
                    name = name,
                    accessToken = it,
                    password = password,
                    email = email,
                    devices = devices,
                    permissions = permissions
                )
            }
        )
    }
}
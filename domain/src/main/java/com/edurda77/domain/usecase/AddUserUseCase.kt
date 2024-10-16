package com.edurda77.domain.usecase

import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
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
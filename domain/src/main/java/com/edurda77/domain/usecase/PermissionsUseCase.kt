package com.edurda77.domain.usecase

import com.edurda77.domain.model.Permissions
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class PermissionsUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<Permissions, DataError> =
        remoteRepository.getPermissions(token)
}
package com.edurda77.domain.usecase

import com.edurda77.domain.model.Permissions
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class PermissionsUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<Permissions, DataError> =
        oldRemoteRepository.getPermissions(token)
}
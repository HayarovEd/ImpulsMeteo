package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.Permission
import com.edurda77.domain.repository.PermissionsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class PermissionsUseCase(
    private val permissionsRepository: PermissionsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<List<Permission>, DataError> {
        return tokenManager.validateFactory(
            data = {
                permissionsRepository.getPermissions(it)
            },
        )
    }
}
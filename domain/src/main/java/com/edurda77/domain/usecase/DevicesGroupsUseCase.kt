package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.repository.DevicesGroupsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DevicesGroupsUseCase(
    private val groupsRepository: DevicesGroupsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(): ResultWork<List<GroupDevice>, DataError> {
        return tokenManager.validateFactory(
            data = {
                groupsRepository.loadGroups(it)
            },
        )
    }
}
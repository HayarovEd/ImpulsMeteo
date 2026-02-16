package com.edurda77.domain.usecase

import com.edurda77.domain.repository.DevicesGroupsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteDevicesGroupUseCase(
    private val devicesGroupsRepository: DevicesGroupsRepository,
    private val tokenManager: TokenManager
) {
    suspend operator fun invoke(
        groupId: String
    ): ResultWork<Unit, DataError> {
        return tokenManager.validateFactory(
            data = {
                devicesGroupsRepository.deleteGroup(
                    accessToken = it,
                    groupId = groupId
                )
            }
        )
    }
}
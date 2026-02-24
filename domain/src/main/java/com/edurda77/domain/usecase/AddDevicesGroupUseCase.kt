package com.edurda77.domain.usecase

import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.repository.DevicesGroupsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

class AddDevicesGroupUseCase(
    private val devicesGroupsRepository: DevicesGroupsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        name: String,
    ): ResultWork<GroupDevice, DataError> {
        if (name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        return tokenManager.validateFactory(
            data = {
                devicesGroupsRepository.insertGroup(
                    accessToken = it,
                    name = name
                )
            }
        )
    }
}
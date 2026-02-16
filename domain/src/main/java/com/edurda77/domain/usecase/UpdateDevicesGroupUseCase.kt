package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.repository.DevicesGroupsRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateDevicesGroupUseCase(
    private val devicesGroupsRepository: DevicesGroupsRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        groupDevice: GroupDevice
    ): ResultWork<GroupDevice, DataError> {
        if (groupDevice.name.isBlank()) return ResultWork.Error(DataError.NameError.NAME_BLANK)
        return tokenManager.validateFactory(
            data = {
                devicesGroupsRepository.updateGroup(
                    accessToken = it,
                    groupDevice = groupDevice
                )
            }
        )
    }
}
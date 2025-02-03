package com.edurda77.domain.usecase

import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DevicesGroupsUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<GroupDevices>, DataError> {
        return remoteRepository.getDevicesGroups(
            token = token,
        )
    }
}
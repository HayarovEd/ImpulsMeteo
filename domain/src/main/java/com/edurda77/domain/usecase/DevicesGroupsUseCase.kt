package com.edurda77.domain.usecase

import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DevicesGroupsUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String
    ): ResultWork<List<GroupDevicesOld>, DataError> {
        return oldRemoteRepository.getDevicesGroups(
            token = token,
        )
    }
}
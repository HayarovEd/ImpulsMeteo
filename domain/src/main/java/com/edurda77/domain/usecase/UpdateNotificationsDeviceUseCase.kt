package com.edurda77.domain.usecase

import com.edurda77.domain.model.Notifications
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateNotificationsDeviceUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        notifications: Notifications,
    ): ResultWork<Unit, DataError> {

        return oldRemoteRepository.updateNotificationsDevice(
            id = id,
            token = token,
            notifications = notifications
        )
    }
}
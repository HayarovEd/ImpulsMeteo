package com.edurda77.domain.usecase

import com.edurda77.domain.model.Notifications
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateNotificationsDeviceUseCase(
    private val remoteRepository: RemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        notifications: Notifications,
    ): ResultWork<Unit, DataError> {

        return remoteRepository.updateNotificationsDevice(
            id = id,
            token = token,
            notifications = notifications
        )
    }
}
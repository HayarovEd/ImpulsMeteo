package com.edurda77.domain.usecase

import com.edurda77.domain.model.NotificationsOld
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateNotificationsDeviceUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {
    suspend operator fun invoke(
        token: String,
        id: Int,
        notificationsOld: NotificationsOld,
    ): ResultWork<Unit, DataError> {

        return oldRemoteRepository.updateNotificationsDevice(
            id = id,
            token = token,
            notificationsOld = notificationsOld
        )
    }
}
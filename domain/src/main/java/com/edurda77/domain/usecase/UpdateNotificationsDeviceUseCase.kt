package com.edurda77.domain.usecase

import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.model.NotificationParam
import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class UpdateNotificationsDeviceUseCase(
    private val devicesRepository: DevicesRepository,
    private val tokenManager: TokenManager,
) {
    suspend operator fun invoke(
        notificationsParam: List<NotificationParam>,
        value: Boolean,
        deviceId: String,
        userId: String,
    ): ResultWork<NotificationDevice, DataError> {

        return tokenManager.validateFactory(
            data = {
                devicesRepository.updateNotificationOfDevice(
                    accessToken = it,
                    notificationsParam = notificationsParam,
                    value = value,
                    deviceId = deviceId,
                    userId = userId
                )
            }
        )
    }
}
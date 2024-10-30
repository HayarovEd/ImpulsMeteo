package com.edurda77.domain.usecase

import com.edurda77.domain.repository.LocalRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import javax.inject.Inject

class RemoveFavoriteUseCase @Inject constructor(
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke(
        deviceId: Int,
    ): ResultWork<Unit, DataError.LocalDateBase> {
        return localRepository.deleteFavorite(
            deviceId = deviceId
        )
    }
}
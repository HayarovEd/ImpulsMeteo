package com.edurda77.domain.usecase

import com.edurda77.domain.repository.LocalRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class DeleteDeviceUseCase(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {
    suspend operator fun invoke(
        token: String,
        isFavorite: Boolean,
        id: Int
    ): ResultWork<Unit, DataError> {
        return when (val result = remoteRepository.deleteDevice(
            token = token,
            id = id
        )) {
            is ResultWork.Error-> {
                ResultWork.Error(result.error)
            }
            is ResultWork.Success -> {
                if (isFavorite) {
                    localRepository.deleteFavorite(id)
                }
                ResultWork.Success(result.data)
            }
        }
    }
}
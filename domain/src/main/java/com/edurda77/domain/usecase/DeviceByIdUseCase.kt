package com.edurda77.domain.usecase

import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.repository.LocalRepository
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class DeviceByIdUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
    private val localRepository: LocalRepository,
) {
    operator fun invoke(
        token: String,
        id: Int,
    ): Flow<ResultWork<SingleDevice, DataError>> {
        return flow {
            localRepository.getAllFavorites().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        emit(ResultWork.Error(collector.error))
                    }

                    is ResultWork.Success -> {
                        when (val result = oldRemoteRepository.getDeviceById(
                            token = token,
                            id = id
                        )) {
                            is ResultWork.Error -> {
                                emit(ResultWork.Error(result.error))
                            }

                            is ResultWork.Success -> {
                                if (collector.data.firstOrNull { it.deviceId == result.data.id } != null) {
                                    emit(
                                        ResultWork.Success(
                                            result.data.copy(isFavorite = true)
                                        )
                                    )
                                } else {
                                    emit(
                                        ResultWork.Success(
                                            result.data.copy(isFavorite = false)
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
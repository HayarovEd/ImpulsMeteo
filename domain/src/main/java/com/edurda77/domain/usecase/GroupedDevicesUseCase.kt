package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.repository.LocalRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.convertToMapGroupedDevices
import com.edurda77.domain.utils.filterGroupedDevices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow


class GroupedDevicesUseCase(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository,
) {

    private val _devices =
        MutableStateFlow<List<Device>>(emptyList())

    operator fun invoke(
        token: String,
        query: String,
        isRefresh: Boolean,
    ): Flow<ResultWork<Map<GroupDevices, List<Device>>, DataError>> {
        return flow {
            localRepository.getAllFavorites().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        emit(ResultWork.Error(collector.error))
                    }

                    is ResultWork.Success -> {
                        if (_devices.value.isEmpty() || isRefresh) {
                            when (val result = remoteRepository.getGroupedDevices(token)) {
                                is ResultWork.Error -> {
                                    emit(ResultWork.Error(result.error))
                                }

                                is ResultWork.Success -> {
                                    _devices.value = result.data
                                }
                            }
                        }
                        val devicesWithFavorite = mutableListOf<Device>()
                        _devices.value.forEach { device ->
                            if (collector.data.firstOrNull { it.deviceId == device.id } != null) {
                                devicesWithFavorite.add(device.copy(isFavorite = true))
                            } else {
                                devicesWithFavorite.add(device.copy(isFavorite = false))
                            }
                        }
                        _devices.value = devicesWithFavorite
                        emit(
                            ResultWork.Success(
                                filterGroupedDevices(
                                    devices = convertToMapGroupedDevices(
                                        devices = _devices.value,
                                    ),
                                    query = query
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
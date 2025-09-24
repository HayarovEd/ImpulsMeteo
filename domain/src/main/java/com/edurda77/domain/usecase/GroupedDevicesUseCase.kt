package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.convertToMapGroupedDevices
import com.edurda77.domain.utils.filterGroupedDevices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext


class GroupedDevicesUseCase(
    private val remoteRepository: RemoteRepository,
) {

    private val _devices =
        MutableStateFlow<List<Device>>(emptyList())


    suspend operator fun invoke(
        token: String,
        query: String,
        isRefresh: Boolean,
        isSorted: Boolean,
    ): ResultWork<Map<GroupDevices, List<Device>>, DataError> {
        return withContext(Dispatchers.IO) {
            if (_devices.value.isEmpty() || isRefresh) {
                val resultGroupedDevicesDif = async { remoteRepository.getGroupedDevices(token) }
                val resultFavoritesDif = async { remoteRepository.getFavorites(token) }
                when (val resultGroupedDevices = resultGroupedDevicesDif.await()) {
                    is ResultWork.Error -> {
                        ResultWork.Error(resultGroupedDevices.error)
                    }
                    is ResultWork.Success -> {
                        when (val resultFavorites = resultFavoritesDif.await()) {
                            is ResultWork.Error -> {
                                ResultWork.Error(resultFavorites.error)
                            }
                            is ResultWork.Success -> {
                                _devices.value = resultGroupedDevices.data
                                val devicesWithFavorite = mutableListOf<Device>()
                                _devices.value.forEach { device ->
                                    if (resultFavorites.data.firstOrNull { it.deviceId == device.id } != null) {
                                        devicesWithFavorite.add(device.copy(isFavorite = true))
                                    } else {
                                        devicesWithFavorite.add(device.copy(isFavorite = false))
                                    }
                                }
                                _devices.value = devicesWithFavorite
                                ResultWork.Success(
                                    filterGroupedDevices(
                                        devices = convertToMapGroupedDevices(
                                            devices = _devices.value,
                                        ),
                                        query = query,
                                        isSorted = isSorted
                                    )
                                )
                            }
                        }
                    }
                }
            } else {
                ResultWork.Success(
                    filterGroupedDevices(
                        devices = convertToMapGroupedDevices(
                            devices = _devices.value,
                        ),
                        query = query,
                        isSorted = isSorted
                    )
                )
            }
        }
    }
}
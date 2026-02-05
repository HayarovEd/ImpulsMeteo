package com.edurda77.domain.usecase

import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.convertToMapGroupedDevices
import com.edurda77.domain.utils.filterGroupedDevices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext


class GroupedDevicesUseCase(
    private val oldRemoteRepository: OldRemoteRepository,
) {

    private val _devices =
        MutableStateFlow<List<DeviceOld>>(emptyList())


    suspend operator fun invoke(
        token: String,
        query: String,
        isRefresh: Boolean,
        isSorted: Boolean,
    ): ResultWork<Map<GroupDevices, List<DeviceOld>>, DataError> {
        return withContext(Dispatchers.IO) {
            if (_devices.value.isEmpty() || isRefresh) {
                val resultGroupedDevicesDif = async { oldRemoteRepository.getGroupedDevices(token) }
                val resultFavoritesDif = async { oldRemoteRepository.getFavorites(token) }
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
                                val devicesWithFavorite = mutableListOf<DeviceOld>()
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
                                            deviceOlds = _devices.value,
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
                            deviceOlds = _devices.value,
                        ),
                        query = query,
                        isSorted = isSorted
                    )
                )
            }
        }
    }
}
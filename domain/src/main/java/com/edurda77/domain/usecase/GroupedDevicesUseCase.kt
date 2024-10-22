package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.convertToMapGroupedDevices
import com.edurda77.domain.utils.filterGroupedDevices
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GroupedDevicesUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
) {
    private val _currentGroupedDevices =
        MutableStateFlow<Map<GroupDevices, List<Device>>>(emptyMap())

    suspend operator fun invoke(
        token: String,
        query: String,
        isRefresh: Boolean,
    ): ResultWork<Map<GroupDevices, List<Device>>, DataError> {

        if (_currentGroupedDevices.value.isEmpty() || isRefresh) {
            when (val result = remoteRepository.getGroupedDevices(token)) {
                is ResultWork.Error -> {
                    return ResultWork.Error(result.error)
                }

                is ResultWork.Success -> {
                    _currentGroupedDevices.value = convertToMapGroupedDevices(
                        devices = result.data
                    )
                }
            }
        }
        return ResultWork.Success(
            filterGroupedDevices(
                devices = _currentGroupedDevices.value,
                query = query
            )
        )
    }
}
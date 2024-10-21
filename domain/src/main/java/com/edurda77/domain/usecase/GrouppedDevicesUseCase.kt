package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.WebSocketMessage
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.repository.WebSocketRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.convertToMapGroupedDevices
import com.edurda77.domain.utils.filterGroupedDevices
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GrouppedDevicesUseCase @Inject constructor(
    private val remoteRepository: RemoteRepository,
    private val webSocketRepository: WebSocketRepository,
) {
    private val _currentGroupedDevices =
        MutableStateFlow<Map<GroupDevices, List<Device>>>(emptyMap())

    operator fun invoke(
        token: String,
        query: String,
        isRefresh: Boolean,
    ): Flow<ResultWork<Map<GroupDevices, List<Device>>, DataError>> {
        return flow {
            webSocketRepository.getStateStream().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        emit(ResultWork.Error(collector.error))
                    }

                    is ResultWork.Success -> {
                        when (collector.data) {
                            is WebSocketMessage.Connect -> {
                                println("web socket open, success ${collector.data.messageWebSocketStart.socketId}")
                            }

                            WebSocketMessage.Error -> {

                            }
                        }
                        if (_currentGroupedDevices.value.isEmpty() || isRefresh) {
                            when (val result = remoteRepository.getGroupedDevices(token)) {
                                is ResultWork.Error -> {
                                    emit(ResultWork.Error(result.error))
                                }

                                is ResultWork.Success -> {
                                    _currentGroupedDevices.value = convertToMapGroupedDevices(
                                        devices = result.data
                                    )
                                }
                            }
                        }
                        emit(
                            ResultWork.Success(
                                filterGroupedDevices(
                                    devices = _currentGroupedDevices.value,
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
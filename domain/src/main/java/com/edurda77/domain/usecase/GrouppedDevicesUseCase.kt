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
    private val _devices =
        MutableStateFlow<List<Device>>(emptyList())

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
                        val broadcastingAuths = mutableListOf<String>()
                        if (_currentGroupedDevices.value.isEmpty() || isRefresh) {
                            when (val result = remoteRepository.getGroupedDevices(token)) {
                                is ResultWork.Error -> {
                                    emit(ResultWork.Error(result.error))
                                }

                                is ResultWork.Success -> {
                                    _currentGroupedDevices.value = convertToMapGroupedDevices(
                                        devices = result.data
                                    )
                                    _devices.value = result.data
                                }
                            }
                        }
                        when (collector.data) {
                            is WebSocketMessage.Connect -> {
                                println("web socket open, success ${collector.data.messageWebSocketStart.socketId}")
                                _devices.value.forEach { device ->
                                    val resultBroadcast = remoteRepository.getBroadcatingAuth(
                                        socketId = collector.data.messageWebSocketStart.socketId,
                                        token = token,
                                        deviceId = device.id
                                    )
                                    when (resultBroadcast) {
                                        is ResultWork.Error -> {
                                            emit(ResultWork.Error(resultBroadcast.error))
                                        }

                                        is ResultWork.Success -> {
                                            broadcastingAuths.add(resultBroadcast.data)
                                        }
                                    }
                                }
                                broadcastingAuths.forEachIndexed { index, s ->
                                    println("web socket open, index $index, auth $s")
                                }
                            }

                            WebSocketMessage.Error -> {

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
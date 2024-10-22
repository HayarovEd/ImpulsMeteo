package com.edurda77.domain.usecase

import com.edurda77.domain.repository.WebSocketRepository
import javax.inject.Inject

class WebSocketUseCase @Inject constructor(
    private val webSocketRepository: WebSocketRepository,
) {
    operator fun invoke(
        token: String,
    ) {
        /*webSocketRepository.getStateStream().collect { collector ->
            when (collector) {
                is ResultWork.Error -> {
                    emit(ResultWork.Error(collector.error))
                }

                is ResultWork.Success -> {
                    val broadcastingAuths = mutableListOf<Subscriber>()

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
                                        broadcastingAuths.add(
                                            Subscriber(
                                                deviceId = device.id,
                                                auth = resultBroadcast.data
                                            )
                                        )
                                    }
                                }
                            }
                            *//*broadcastingAuths.forEach {
                                webSocketRepository.sendAction(
                                    event = SUBSCRIBE_EVENT,
                                    deviceId = it.deviceId,
                                    auth = it.auth
                                )
                            }*//*
                        }

                        is WebSocketMessage.SuccessSbscribe -> {
                            //println("web socket open, success subscribe ${collector.data.successSubscribe.event}")
                            //println("web socket open, success subscribe ${collector.data.successSubscribe.channel}")
                        }

                        is WebSocketMessage.DeviceEvent -> {
                            println("web socket open, success device ${collector.data.device.id}")
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
        }*/
    }
}
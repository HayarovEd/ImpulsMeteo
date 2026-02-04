package com.edurda77.domain.usecase

import com.edurda77.domain.model.Device
import com.edurda77.domain.model.Subscriber
import com.edurda77.domain.model.WebSocketMessage
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.repository.WebSocketRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.SUBSCRIBE_EVENT
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class WebSocketUseCase(
    private val webSocketRepository: WebSocketRepository,
    private val oldRemoteRepository: OldRemoteRepository,
) {
    operator fun invoke(
        token: String,
        ids: List<Int>
    ): Flow<ResultWork<Device, DataError>> {
        return flow {
            webSocketRepository.getStateStream().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        emit(ResultWork.Error(collector.error))
                    }

                    is ResultWork.Success -> {
                        val broadcastingAuths = mutableListOf<Subscriber>()

                        when (collector.data) {
                            is WebSocketMessage.Connect -> {
                                ids.forEach { id ->
                                    val resultBroadcast = oldRemoteRepository.getBroadcatingAuth(
                                        socketId = collector.data.messageWebSocketStart.socketId,
                                        token = token,
                                        deviceId = id
                                    )
                                    when (resultBroadcast) {
                                        is ResultWork.Error -> {
                                            emit(ResultWork.Error(resultBroadcast.error))
                                        }

                                        is ResultWork.Success -> {
                                            broadcastingAuths.add(
                                                Subscriber(
                                                    deviceId = id,
                                                    auth = resultBroadcast.data
                                                )
                                            )
                                        }
                                    }
                                }
                                broadcastingAuths.forEach {
                                    webSocketRepository.sendAction(
                                        event = SUBSCRIBE_EVENT,
                                        deviceId = it.deviceId,
                                        auth = it.auth
                                    )
                                }
                            }

                            is WebSocketMessage.SuccessSbscribe -> {
                                println("web socket open, success subscribe ${collector.data.successSubscribe.channel}")
                            }

                            is WebSocketMessage.DeviceEvent -> {
                                emit(ResultWork.Success(collector.data.device))
                            }
                        }

                    }
                }
            }
        }
    }

}
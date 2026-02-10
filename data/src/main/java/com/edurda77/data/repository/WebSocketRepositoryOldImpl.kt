package com.edurda77.data.repository

import com.edurda77.data.mapper.convertDeviceMessageToDevice
import com.edurda77.data.mapper.convertInitMessage
import com.edurda77.data.mapper.convertToSuccessSubscribe
import com.edurda77.data.remote.message_dto.Content
import com.edurda77.data.remote.message_dto.MessageEvent
import com.edurda77.domain.model.WebSocketMessageOld
import com.edurda77.domain.repository.WebSocketRepositoryOld
import com.edurda77.domain.utils.DEVICE_EVENT
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ESTABLISHED
import com.edurda77.domain.utils.EVENT_CHANNEL_PREFIX
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.SUCCESSES
import com.edurda77.domain.utils.WEB_SOCKET_URL
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.url
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

class WebSocketRepositoryOldImpl(
    private val client: HttpClient
) : WebSocketRepositoryOld {
    private var session: WebSocketSession? = null


    override fun getStateStream(): Flow<ResultWork<WebSocketMessageOld, DataError.WebSocketError>> {
        return flow<ResultWork<WebSocketMessageOld, DataError.WebSocketError>> {
            session = client.webSocketSession {
                url(WEB_SOCKET_URL)
            }
            session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .collect {
                    val message = it.readText()
                    if (message.contains(ESTABLISHED)) {
                        emit(ResultWork.Success(WebSocketMessageOld.Connect(convertInitMessage(message))))
                    }
                    if (message.contains(SUCCESSES)) {
                        emit(
                            ResultWork.Success(
                                WebSocketMessageOld.SuccessSbscribe(
                                    convertToSuccessSubscribe(message)
                                )
                            )
                        )
                    }
                    if (message.contains(DEVICE_EVENT)) {
                        emit(
                            ResultWork.Success(
                                WebSocketMessageOld.DeviceEvent(
                                    convertDeviceMessageToDevice(message)
                                )
                            )
                        )
                    }
                }
        }.catch {
            //emit(ResultWork.Error(DataError.WebSocketError.NOT_CONNECT))
        }
    }

    override suspend fun sendAction(
        event: String,
        deviceId: Int,
        auth: String,
    ) {
        session?.outgoing?.send(
            Frame.Text(
                Json.encodeToString(
                    MessageEvent(
                        event = event,
                        content = Content(
                            auth = auth,
                            channel = "${EVENT_CHANNEL_PREFIX}$deviceId"
                        )
                    )
                )
            )
        )
    }

    override suspend fun close() {
        session?.close()
        session = null
    }
}
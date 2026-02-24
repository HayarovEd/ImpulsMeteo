package com.edurda77.data.repository

import com.edurda77.data.mapper.convertToDevice
import com.edurda77.data.mapper.toFavorite
import com.edurda77.data.mapper.toParam
import com.edurda77.data.mapper.toUser
import com.edurda77.data.remote.newDtos.device.DeviceWsDto
import com.edurda77.data.remote.newDtos.favorite.FavoriteDto
import com.edurda77.data.remote.newDtos.param.ParamDto
import com.edurda77.data.remote.newDtos.user.UserDto
import com.edurda77.data.remote.newDtos.web_socket.WsContentDto
import com.edurda77.domain.model.newModels.WebSocketMessage
import com.edurda77.domain.repository.WebSocketRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.NEW_WEB_SOCKET_URL
import com.edurda77.domain.utils.ResultWork
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.parameter
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

class WebSocketRepositoryImpl(
    private val client: HttpClient
) : WebSocketRepository {
    private var session: WebSocketSession? = null

    override fun getStateStream(
        accessToken: String,
    ): Flow<ResultWork<WebSocketMessage, DataError.WebSocketError>> {
        return flow<ResultWork<WebSocketMessage, DataError.WebSocketError>> {
            session = client.webSocketSession {
                url(NEW_WEB_SOCKET_URL)
                parameter("token", accessToken)
            }
            session!!
                .incoming
                .consumeAsFlow()
                .filterIsInstance<Frame.Text>()
                .collect {
                    val message = it.readText()
                    val json = Json {
                        ignoreUnknownKeys = true
                        coerceInputValues = true
                        encodeDefaults = true
                        explicitNulls = false
                    }
                    when {
                        message.contains("DEVICE_CREATED") -> {
                            val result = json.decodeFromString<WsContentDto<DeviceWsDto>>(message)
                            emit(ResultWork.Success(WebSocketMessage.DeviceCreate(result.content.convertToDevice())))
                        }

                        message.contains("DEVICE_UPDATED") -> {
                            val result = json.decodeFromString<WsContentDto<DeviceWsDto>>(message)
                            emit(ResultWork.Success(WebSocketMessage.DeviceUpdate(result.content.convertToDevice())))
                        }

                        message.contains("DEVICE_DELETED") -> {
                            val result = json.decodeFromString<WsContentDto<String>>(message)
                            emit(ResultWork.Success(WebSocketMessage.DeviceDelete(result.content)))
                        }

                        message.contains("USER_CREATED") -> {
                            val result = json.decodeFromString<WsContentDto<UserDto>>(message)
                            emit(ResultWork.Success(WebSocketMessage.UserCreate(result.content.toUser())))
                        }

                        message.contains("USER_UPDATED") -> {
                            val result = json.decodeFromString<WsContentDto<UserDto>>(message)
                            emit(ResultWork.Success(WebSocketMessage.UserUpdate(result.content.toUser())))
                        }

                        message.contains("USER_DELETED") -> {
                            val result = json.decodeFromString<WsContentDto<String>>(message)
                            emit(ResultWork.Success(WebSocketMessage.UserDelete(result.content)))
                        }

                        message.contains("FAVORITE_UPDATED") -> {
                            val result =
                                json.decodeFromString<WsContentDto<List<FavoriteDto>>>(message)
                            emit(ResultWork.Success(WebSocketMessage.FavoriteUpdate(result.content.map { it.toFavorite() })))
                        }

                        message.contains("PARAM_DATA_UPDATE") -> {
                            val result = json.decodeFromString<WsContentDto<DeviceWsDto>>(message)
                            emit(ResultWork.Success(WebSocketMessage.ParamDataUpdate(result.content.convertToDevice())))
                        }

                        message.contains("PARAM_DATA_UPDATE") -> {
                            val result = json.decodeFromString<WsContentDto<ParamDto>>(message)
                            emit(ResultWork.Success(WebSocketMessage.ParamUpdate(result.content.toParam())))
                        }
                    }
                }
        }.catch { e->
            print(e.message)
            emit(ResultWork.Error(DataError.WebSocketError.NOT_CONNECT))
        }
    }

    override suspend fun close() {
        session?.close()
        session = null
    }
}
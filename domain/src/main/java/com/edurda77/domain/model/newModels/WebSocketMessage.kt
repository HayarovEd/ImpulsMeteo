package com.edurda77.domain.model.newModels

sealed interface WebSocketMessage {
    class DeviceCreate(val device: Device) : WebSocketMessage
    class DeviceUpdate(val device: Device) : WebSocketMessage
    class DeviceDelete(val id: String) : WebSocketMessage
    class UserCreate(val user: User) : WebSocketMessage
    class UserUpdate(val user: User) : WebSocketMessage
    class UserDelete(val id: String) : WebSocketMessage
    class FavoriteUpdate(val favorites: List<Favorite>) : WebSocketMessage
    class ParamDataUpdate(val device: Device) : WebSocketMessage
    class ParamUpdate(val param: Param) : WebSocketMessage
}
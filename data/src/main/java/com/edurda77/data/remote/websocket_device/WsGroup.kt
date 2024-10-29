package com.edurda77.data.remote.websocket_device


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WsGroup(
    @SerialName("created_at")
    val createdAt: String?,
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String,
    @SerialName("parent_id")
    val parentId: String?,
    @SerialName("pivot")
    val wsPivotGroup: WsPivotGroup,
    @SerialName("updated_at")
    val updatedAt: String?
)
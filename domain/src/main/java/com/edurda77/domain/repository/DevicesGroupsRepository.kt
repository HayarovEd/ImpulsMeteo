package com.edurda77.domain.repository

import com.edurda77.domain.model.GroupDevice
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface DevicesGroupsRepository {
    suspend fun loadGroups(accessToken: String): ResultWork<List<GroupDevice>, DataError>
    suspend fun insertGroup(accessToken: String, name: String): ResultWork<GroupDevice, DataError>
    suspend fun updateGroup(
        accessToken: String,
        groupDevice: GroupDevice
    ): ResultWork<GroupDevice, DataError>

    suspend fun deleteGroup(accessToken: String, groupId: String): ResultWork<Unit, DataError>
}
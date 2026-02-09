package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface DevicesGroupsRepository {
    suspend fun loadGroups(accessToken: String): ResultWork<List<GroupDevice>, DataError>
}
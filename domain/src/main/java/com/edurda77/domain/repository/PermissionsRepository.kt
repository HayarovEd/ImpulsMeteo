package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.Permission
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface PermissionsRepository {
    suspend fun getPermissions(accessToken: String): ResultWork<List<Permission>, DataError>
}
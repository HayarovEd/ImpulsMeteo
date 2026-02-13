package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.DeviceUser
import com.edurda77.domain.model.newModels.PermissionUser
import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.model.newModels.UserUi
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface UsersRepository {
    suspend fun getUsers(accessToken: String): ResultWork<List<User>, DataError>
    suspend fun insertUser(
        accessToken: String,
        name: String,
        password: String,
        email: String,
        devices: List<DeviceUser>,
        permissions: List<PermissionUser>
    ): ResultWork<User, DataError>

    suspend fun updateUser(accessToken: String, userUi: UserUi): ResultWork<User, DataError>
    suspend fun deleteUser(accessToken: String, userId: String): ResultWork<Unit, DataError>
}
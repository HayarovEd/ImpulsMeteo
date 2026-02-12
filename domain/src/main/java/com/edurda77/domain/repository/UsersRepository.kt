package com.edurda77.domain.repository

import com.edurda77.domain.model.newModels.User
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork

interface UsersRepository {
    suspend fun getUsers(accessToken: String): ResultWork<List<User>, DataError>
}
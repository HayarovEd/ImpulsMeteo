package com.edurda77.domain.repository

import com.edurda77.domain.model.Auth
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setAuthorization(auth: Auth): ResultWork<Unit, DataError.DataStore>
    fun readAuthorization(): Flow<ResultWork<Auth, DataError.DataStore>>
    suspend fun deleteAuthorization(): ResultWork<Unit, DataError.DataStore>
}
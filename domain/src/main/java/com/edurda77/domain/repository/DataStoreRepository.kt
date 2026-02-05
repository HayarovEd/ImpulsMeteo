package com.edurda77.domain.repository

import com.edurda77.domain.model.AuthOld
import com.edurda77.domain.model.LastAuthData
import com.edurda77.domain.model.Token
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun setAuthorization(authOld: AuthOld): ResultWork<Unit, DataError.DataStore>
    fun readAuthorization(): Flow<ResultWork<AuthOld, DataError.DataStore>>
    suspend fun deleteAuthorization(): ResultWork<Unit, DataError.DataStore>
    suspend fun setLocalAuthorization(lastAuthData: LastAuthData): ResultWork<Unit, DataError.DataStore>
    fun getLocalAuthorization(): Flow<ResultWork<LastAuthData, DataError.DataStore>>
    suspend fun saveTokens(token: Token): ResultWork<Unit, DataError.DataStore>
    fun readTokens(): Flow<ResultWork<Token, DataError.DataStore>>
    suspend fun deleteTokens(): ResultWork<Unit, DataError.DataStore>
    suspend fun readStateTokens(): ResultWork<Token, DataError.DataStore>
}
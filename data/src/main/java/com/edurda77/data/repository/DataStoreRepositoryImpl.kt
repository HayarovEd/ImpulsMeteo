package com.edurda77.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.edurda77.data.handler.handleRead
import com.edurda77.data.handler.handleReadFlow
import com.edurda77.data.handler.handleWrite
import com.edurda77.domain.model.AuthOld
import com.edurda77.domain.model.LastAuthData
import com.edurda77.domain.model.Token
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.ACCESS_TOKEN_LABEL
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EXPIRED_LABEL
import com.edurda77.domain.utils.LAST_EMAIL
import com.edurda77.domain.utils.LAST_PASSWORD
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.REFRESH_TOKEN_LABEL
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.TOKEN_LABEL
import com.edurda77.domain.utils.USER_ID_LABEL
import com.edurda77.domain.utils.convertToLocalDateTimeOld
import com.edurda77.domain.utils.convertToStringDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun setAuthorization(
        authOld: AuthOld
    ): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings[FIELD_TOKEN_LABEL] = authOld.accessToken
                settings[FIELD_EXPIRED_LABEL] = convertToStringDateTime(authOld.expiresAt)
                settings[FIELD_USER_ID_LABEL] = authOld.id
            }
        }
    }

    override fun readAuthorization(): Flow<ResultWork<AuthOld, DataError.DataStore>> {
        return handleReadFlow {
            dataStore.data.map {
                AuthOld(
                    accessToken = it[FIELD_TOKEN_LABEL] ?: "",
                    expiresAt = convertToLocalDateTimeOld(it[FIELD_EXPIRED_LABEL] ?: ""),
                    id = it[FIELD_USER_ID_LABEL] ?: NEGATIVE_ID,
                )
            }
        }
    }

    override suspend fun deleteAuthorization(): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings.remove(FIELD_TOKEN_LABEL)
                settings.remove(FIELD_EXPIRED_LABEL)
                settings.remove(FIELD_USER_ID_LABEL)
            }
        }
    }

    override suspend fun setLocalAuthorization(lastAuthData: LastAuthData): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings[FIELD_LAST_EMAIL] = lastAuthData.email
                settings[FIELD_LAST_PASSWORD] = lastAuthData.password
            }
        }
    }

    override fun getLocalAuthorization(): Flow<ResultWork<LastAuthData, DataError.DataStore>> {
        return handleReadFlow {
            dataStore.data.map {
                LastAuthData(
                    email = it[FIELD_LAST_EMAIL] ?: "",
                    password = it[FIELD_LAST_PASSWORD] ?: "",
                )
            }
        }
    }

    override suspend fun saveTokens(
        token: Token
    ): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings[FIELD_ACCESS_TOKEN] = token.accessToken
                settings[FIELD_REFRESH_TOKEN] = token.refreshToken
            }
        }
    }

    override fun readTokens(): Flow<ResultWork<Token, DataError.DataStore>> {
        return handleReadFlow {
            //dataStore.data.first()[FIELD_PASSWORD_LABEL] ?: ""
            dataStore.data.map {
                Token(
                    accessToken = it[FIELD_ACCESS_TOKEN] ?: "",
                    refreshToken = it[FIELD_REFRESH_TOKEN] ?: "",
                )
            }
        }
    }

    override suspend fun readStateTokens(): ResultWork<Token, DataError.DataStore> {
        return handleRead{
            Token(
                accessToken = dataStore.data.first()[FIELD_ACCESS_TOKEN] ?: "",
                refreshToken = dataStore.data.first()[FIELD_REFRESH_TOKEN] ?: "",
            )
        }
    }

    override suspend fun deleteTokens(): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings.remove(FIELD_ACCESS_TOKEN)
                settings.remove(FIELD_REFRESH_TOKEN)
            }
        }
    }



    companion object {
        val FIELD_TOKEN_LABEL = stringPreferencesKey(TOKEN_LABEL)
        val FIELD_EXPIRED_LABEL = stringPreferencesKey(EXPIRED_LABEL)
        val FIELD_USER_ID_LABEL = intPreferencesKey(USER_ID_LABEL)
        val FIELD_LAST_EMAIL = stringPreferencesKey(LAST_EMAIL)
        val FIELD_LAST_PASSWORD = stringPreferencesKey(LAST_PASSWORD)
        val FIELD_ACCESS_TOKEN = stringPreferencesKey(ACCESS_TOKEN_LABEL)
        val FIELD_REFRESH_TOKEN = stringPreferencesKey(REFRESH_TOKEN_LABEL)
    }
}
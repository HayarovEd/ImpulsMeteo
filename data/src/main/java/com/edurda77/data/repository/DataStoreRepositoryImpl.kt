package com.edurda77.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.edurda77.data.handler.handleRead
import com.edurda77.data.handler.handleWrite
import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.LastAuthData
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EXPIRED_LABEL
import com.edurda77.domain.utils.LAST_EMAIL
import com.edurda77.domain.utils.LAST_PASSWORD
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.TOKEN_LABEL
import com.edurda77.domain.utils.USER_ID_LABEL
import com.edurda77.domain.utils.convertToLocalDateTime
import com.edurda77.domain.utils.convertToStringDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun setAuthorization(
        auth: Auth
    ): ResultWork<Unit, DataError.DataStore> {
        return handleWrite {
            dataStore.edit { settings ->
                settings[FIELD_TOKEN_LABEL] = auth.accessToken
                settings[FIELD_EXPIRED_LABEL] = convertToStringDateTime(auth.expiresAt)
                settings[FIELD_USER_ID_LABEL] = auth.id
            }
        }
    }

    override fun readAuthorization(): Flow<ResultWork<Auth, DataError.DataStore>> {
        return handleRead {
            dataStore.data.map {
                Auth(
                    accessToken = it[FIELD_TOKEN_LABEL] ?: "",
                    expiresAt = convertToLocalDateTime(it[FIELD_EXPIRED_LABEL] ?: ""),
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
        return handleRead {
            dataStore.data.map {
                LastAuthData(
                    email = it[FIELD_LAST_EMAIL] ?: "",
                    password = it[FIELD_LAST_PASSWORD] ?: "",
                )
            }
        }
    }


    companion object {
        val FIELD_TOKEN_LABEL = stringPreferencesKey(TOKEN_LABEL)
        val FIELD_EXPIRED_LABEL = stringPreferencesKey(EXPIRED_LABEL)
        val FIELD_USER_ID_LABEL = intPreferencesKey(USER_ID_LABEL)
        val FIELD_LAST_EMAIL = stringPreferencesKey(LAST_EMAIL)
        val FIELD_LAST_PASSWORD = stringPreferencesKey(LAST_PASSWORD)
    }
}
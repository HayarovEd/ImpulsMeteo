package com.edurda77.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.edurda77.domain.model.Auth
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.EXPIRED_LABEL
import com.edurda77.domain.utils.NEGATIVE_USER_ID
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.TOKEN_LABEL
import com.edurda77.domain.utils.USER_ID_LABEL
import com.edurda77.domain.utils.convertToLocalDateTime
import com.edurda77.domain.utils.convertToStringDateTime
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : DataStoreRepository {

    override suspend fun setAuthorization(
        auth: Auth
    ): ResultWork<Unit, DataError.DataStore> {
        return try {
            dataStore.edit { settings ->
                settings[FIELD_TOKEN_LABEL] = auth.accessToken
                settings[FIELD_EXPIRED_LABEL] = convertToStringDateTime(auth.expiresAt)
                settings[FIELD_USER_ID_LABEL] = auth.id
            }
            ResultWork.Success(Unit)
        } catch (e: Exception) {
            ResultWork.Error(DataError.DataStore.ERROR_WRITE_DATA)
        }
    }

    override fun readAuthorization(): Flow<ResultWork<Auth, DataError.DataStore>> {
        return flow<ResultWork<Auth, DataError.DataStore>> {
            dataStore.data.map {
                Auth(
                    accessToken = it[FIELD_TOKEN_LABEL] ?: "",
                    expiresAt = convertToLocalDateTime(it[FIELD_EXPIRED_LABEL] ?: ""),
                    id = it[FIELD_USER_ID_LABEL] ?: NEGATIVE_USER_ID,
                )
            }.collect { collector ->
                emit(
                    ResultWork.Success(collector)
                )
            }
        }.catch {
            emit(
                ResultWork.Error(DataError.DataStore.ERROR_READ_DATA)
            )
        }
    }

    override suspend fun deleteAuthorization(): ResultWork<Unit, DataError.DataStore> {
        return try {
            dataStore.edit { settings ->
                settings.remove(FIELD_TOKEN_LABEL)
                settings.remove(FIELD_EXPIRED_LABEL)
                settings.remove(FIELD_USER_ID_LABEL)
            }
            ResultWork.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            ResultWork.Error(DataError.DataStore.ERROR_READ_DATA)
        }
    }

    /*override suspend fun setLocalAuthorization(authorization: Authorization) {
        dataStore.edit { settings ->
            settings[FIELD_LAST_EMAIL] = authorization.email
            settings[FIELD_LAST_PASSWORD] = authorization.password
        }
    }

    override fun getLocalAuthorization(): Flow<Authorization> {
        return dataStore.data.map {
            Authorization(
                email = it[FIELD_LAST_EMAIL] ?: "",
                password = it[FIELD_LAST_PASSWORD] ?: "",
            )
        }
    }*/


    companion object {
        val FIELD_TOKEN_LABEL = stringPreferencesKey(TOKEN_LABEL)
        val FIELD_EXPIRED_LABEL = stringPreferencesKey(EXPIRED_LABEL)
        val FIELD_USER_ID_LABEL = intPreferencesKey(USER_ID_LABEL)

        /*val FIELD_LAST_EMAIL = stringPreferencesKey(LAST_EMAIL)
        val FIELD_LAST_PASSWORD = stringPreferencesKey(LAST_PASSWORD)*/
    }
}
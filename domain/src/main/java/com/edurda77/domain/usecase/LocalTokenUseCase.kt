package com.edurda77.domain.usecase

import com.edurda77.domain.model.Auth
import com.edurda77.domain.model.LocalAuthResult
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class LocalTokenUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    operator fun invoke(): Flow<ResultWork<Auth, DataError.DataStore>> {
        return dataStoreRepository.readAuthorization()
    }
}
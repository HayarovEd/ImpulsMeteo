package com.edurda77.domain.usecase

import com.edurda77.domain.model.LocalAuthResult
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

class AuthCheckUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
) {
    suspend operator fun invoke(): Flow<LocalAuthResult> {
        return flow {
            dataStoreRepository.readAuthorization().collect { collectAuth ->
                when (collectAuth) {
                    is ResultWork.Error -> {
                        emit(LocalAuthResult.LocalNotSession)
                    }

                    is ResultWork.Success -> {
                        val timeZone = TimeZone.currentSystemDefault()
                        if (Clock.System.now()
                                .toLocalDateTime(timeZone) >= collectAuth.data.expiresAt
                        ) {
                            emit(LocalAuthResult.LocalNotSession)
                        } else emit(LocalAuthResult.LocalSession)
                    }
                }
            }
        }
    }
}
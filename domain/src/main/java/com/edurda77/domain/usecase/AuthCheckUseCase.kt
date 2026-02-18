package com.edurda77.domain.usecase

import com.edurda77.domain.model.newModels.LocalAuthResult
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.JwtRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Clock
import kotlin.time.ExperimentalTime


class AuthCheckUseCase(
    private val dataStoreRepository: DataStoreRepository,
    private val remoteRepository: RemoteRepository,
    private val jwtRepository: JwtRepository,
) {
    @OptIn(ExperimentalTime::class)
    operator fun invoke(): Flow<LocalAuthResult> {
        return flow {
            dataStoreRepository.readTokens().collect { tokenFlow ->
                when (tokenFlow) {
                    is ResultWork.Error -> {
                        emit(LocalAuthResult.LocalNotSession)
                    }

                    is ResultWork.Success -> {
                        val expiredAccess = jwtRepository.decodeJwt(tokenFlow.data.accessToken)
                        val currentTimeLong = Clock.System.now().epochSeconds
                        if (expiredAccess == null || expiredAccess < currentTimeLong) {
                            val expiredRefresh =
                                jwtRepository.decodeJwt(tokenFlow.data.refreshToken)
                            if (expiredRefresh == null || expiredRefresh < currentTimeLong) {
                                emit(LocalAuthResult.LocalNotSession)
                            } else {
                                when (val remoteRefresh =
                                    remoteRepository.refresh(tokenFlow.data.refreshToken)) {
                                    is ResultWork.Error -> emit(LocalAuthResult.LocalNotSession)
                                    is ResultWork.Success -> {
                                        dataStoreRepository.saveTokens(remoteRefresh.data)
                                        emit(LocalAuthResult.LocalSession)
                                    }
                                }
                            }
                        } else {
                            emit(LocalAuthResult.LocalSession)
                        }
                    }
                }
            }
        }
    }
}
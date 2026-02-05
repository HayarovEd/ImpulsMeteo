package com.edurda77.domain.usecase

import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.JwtRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlin.time.Clock

class TokenManager(
    private val dataStoreRepository: DataStoreRepository,
    private val remoteRepository: RemoteRepository,
    private val jwtRepository: JwtRepository,
) {
    suspend fun <D> validateFactory(
        data: suspend (String) -> ResultWork<D, DataError>,
    ): ResultWork<D, DataError> {
        return when (val tokens = dataStoreRepository.readStateTokens()) {
            is ResultWork.Error -> {
                ResultWork.Error(DataError.TokenError.TOKEN_EXPIRED)
            }

            is ResultWork.Success -> {
                val expiredAccess = jwtRepository.decodeJwt(tokens.data.accessToken)
                val currentTimeLong = Clock.System.now().epochSeconds
                if (expiredAccess == null || expiredAccess < currentTimeLong) {
                    val expiredRefresh =
                        jwtRepository.decodeJwt(tokens.data.refreshToken)
                    if (expiredRefresh == null || expiredRefresh < currentTimeLong) {
                        ResultWork.Error(DataError.TokenError.TOKEN_EXPIRED)
                    } else {
                        when (val remoteRefresh =
                            remoteRepository.refresh(tokens.data.refreshToken)) {
                            is ResultWork.Error -> ResultWork.Error(DataError.TokenError.TOKEN_EXPIRED)
                            is ResultWork.Success -> {
                                dataStoreRepository.saveTokens(remoteRefresh.data)
                                data(remoteRefresh.data.accessToken)
                            }
                        }
                    }
                } else {
                    data(tokens.data.accessToken)
                }
            }
        }
    }
}
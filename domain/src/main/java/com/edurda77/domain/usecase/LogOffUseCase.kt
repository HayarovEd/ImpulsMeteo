package com.edurda77.domain.usecase

import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class LogOffUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(): ResultWork<Unit, DataError.DataStore> =
        dataStoreRepository.deleteAuthorization()
}
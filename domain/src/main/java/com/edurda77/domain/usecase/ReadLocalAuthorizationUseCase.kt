package com.edurda77.domain.usecase



import com.edurda77.domain.model.newModels.LastAuthData
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import kotlinx.coroutines.flow.Flow


class ReadLocalAuthorizationUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    operator fun invoke(
    ): Flow<ResultWork<LastAuthData, DataError.DataStore>> {
        return dataStoreRepository.getLocalAuthorization()
    }
}
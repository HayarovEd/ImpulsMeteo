package com.edurda77.domain.usecase


import com.edurda77.domain.model.newModels.LastAuthData
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork


class SaveLocalAuthorizationUseCase(
    private val dataStoreRepository: DataStoreRepository
) {
    suspend operator fun invoke(
        email: String,
        password: String
    ): ResultWork<Unit, DataError.DataStore> {
        return dataStoreRepository.setLocalAuthorization(
            LastAuthData(
                email = email,
                password = password,
            )
        )
    }
}
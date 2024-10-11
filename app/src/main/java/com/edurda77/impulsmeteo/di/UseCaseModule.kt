package com.edurda77.impulsmeteo.di


import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.usecase.AuthCheckUseCase
import com.edurda77.domain.usecase.GrouppedDevicesUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.LoginUseCase
import com.edurda77.domain.usecase.ReadLocalAuthorizationUseCase
import com.edurda77.domain.usecase.SaveLocalAuthorizationUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesAuthCheckUseCase(dataStoreRepository: DataStoreRepository): AuthCheckUseCase {
        return AuthCheckUseCase(dataStoreRepository)
    }

    @Provides
    @Singleton
    fun providesLoginUseCase(
        remoteRepository: RemoteRepository,
        dataStoreRepository: DataStoreRepository
    ): LoginUseCase {
        return LoginUseCase(
            remoteRepository = remoteRepository,
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesReadLocalAuthorizationUseCase(
        dataStoreRepository: DataStoreRepository
    ): ReadLocalAuthorizationUseCase {
        return ReadLocalAuthorizationUseCase(
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesSaveLocalAuthorizationUseCase(
        dataStoreRepository: DataStoreRepository
    ): SaveLocalAuthorizationUseCase {
        return SaveLocalAuthorizationUseCase(
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesLoggedUserUseCase(remoteRepository: RemoteRepository): LoggedUserUseCase {
        return LoggedUserUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesGrouppedDevicesUseCase(remoteRepository: RemoteRepository): GrouppedDevicesUseCase {
        return GrouppedDevicesUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesLocalTokenUseCase(dataStoreRepository: DataStoreRepository): LocalTokenUseCase {
        return LocalTokenUseCase(dataStoreRepository = dataStoreRepository)
    }

    @Provides
    @Singleton
    fun providesLocalLogOffUseCase(dataStoreRepository: DataStoreRepository): LogOffUseCase {
        return LogOffUseCase(dataStoreRepository = dataStoreRepository)
    }

    /*

    @Provides
    @Singleton
    fun providesLocalSessionUseCase(
        dataStoreRepository: DataStoreRepository
    ): LocalSessionUseCase {
        return LocalSessionUseCase(
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesMonitorVideoUseCase(
        remoteRepository: RemoteRepository
    ): MonitorVideoUseCase {
        return MonitorVideoUseCase(
            remoteRepository = remoteRepository
        )
    }

    @Provides
    @Singleton
    fun providesLogoffUseCase(
        dataStoreRepository: DataStoreRepository
    ): LogoffUseCase {
        return LogoffUseCase(
            dataStoreRepository = dataStoreRepository
        )
    }

    @Provides
    @Singleton
    fun providesLocalAuthorizationUseCase(
        dataStoreRepository: DataStoreRepository,
        remoteRepository: RemoteRepository,
    ): LocalAuthorizationUseCase {
        return LocalAuthorizationUseCase(
            dataStoreRepository = dataStoreRepository,
            remoteRepository = remoteRepository
        )
    }



    @Provides
    @Singleton
    fun providesRecordVideoUseCase(
        storageRepository: StorageRepository
    ): RecordVideoUseCase {
        return RecordVideoUseCase(
            storageRepository = storageRepository
        )
    }*/

}
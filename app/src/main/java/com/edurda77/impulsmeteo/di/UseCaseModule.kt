package com.edurda77.impulsmeteo.di


import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.usecase.AddDeviceUseCase
import com.edurda77.domain.usecase.AddUserUseCase
import com.edurda77.domain.usecase.AuthCheckUseCase
import com.edurda77.domain.usecase.DeleteUserUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.GrouppedDevicesUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.LoginUseCase
import com.edurda77.domain.usecase.PermissionsUseCase
import com.edurda77.domain.usecase.ReadLocalAuthorizationUseCase
import com.edurda77.domain.usecase.SaveLocalAuthorizationUseCase
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateUserUseCase
import com.edurda77.domain.usecase.UsersUseCase
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

    @Provides
    @Singleton
    fun providesAddDeviceUseCase(remoteRepository: RemoteRepository): AddDeviceUseCase {
        return AddDeviceUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesUsersUseCase(remoteRepository: RemoteRepository): UsersUseCase {
        return UsersUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesPermissionsUseCase(remoteRepository: RemoteRepository): PermissionsUseCase {
        return PermissionsUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesAddUserUseCase(remoteRepository: RemoteRepository): AddUserUseCase {
        return AddUserUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesDeleteUserUseCase(remoteRepository: RemoteRepository): DeleteUserUseCase {
        return DeleteUserUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesUpdateUserUseCase(remoteRepository: RemoteRepository): UpdateUserUseCase {
        return UpdateUserUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesDevicesGroupsUseCase(remoteRepository: RemoteRepository): DevicesGroupsUseCase {
        return DevicesGroupsUseCase(remoteRepository = remoteRepository)
    }

    @Provides
    @Singleton
    fun providesUnitsUseCase(remoteRepository: RemoteRepository): UnitsUseCase {
        return UnitsUseCase(remoteRepository = remoteRepository)
    }
}

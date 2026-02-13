package com.edurda77.impulsmeteo.di

import com.edurda77.domain.usecase.AddDeviceUseCase
import com.edurda77.domain.usecase.AddDevicesGroupUseCase
import com.edurda77.domain.usecase.UpdateFavoriteUseCase
import com.edurda77.domain.usecase.AddUnitUseCase
import com.edurda77.domain.usecase.AddUserUseCase
import com.edurda77.domain.usecase.AuthCheckUseCase
import com.edurda77.domain.usecase.CloseWebsocketUseCase
import com.edurda77.domain.usecase.DeleteDeviceUseCase
import com.edurda77.domain.usecase.DeleteDevicesGroupUseCase
import com.edurda77.domain.usecase.DeleteParamUseCase
import com.edurda77.domain.usecase.DeleteUnitUseCase
import com.edurda77.domain.usecase.DeleteUserUseCase
import com.edurda77.domain.usecase.DeviceByIdUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.DevicesUseCase
import com.edurda77.domain.usecase.HistoryUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.LoginUseCase
import com.edurda77.domain.usecase.PermissionsUseCase
import com.edurda77.domain.usecase.ReadLocalAuthorizationUseCase
import com.edurda77.domain.usecase.RemoveFavoriteUseCase
import com.edurda77.domain.usecase.SaveLocalAuthorizationUseCase
import com.edurda77.domain.usecase.TokenManager
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateDeviceUseCase
import com.edurda77.domain.usecase.UpdateDevicesGroupUseCase
import com.edurda77.domain.usecase.UpdateNotificationsDeviceUseCase
import com.edurda77.domain.usecase.UpdateParamUseCase
import com.edurda77.domain.usecase.UpdateUnitUseCase
import com.edurda77.domain.usecase.UpdateUserUseCase
import com.edurda77.domain.usecase.UsersUseCase
import com.edurda77.domain.usecase.WebSocketUseCase
import com.edurda77.domain.usecase.WebSocketUseCaseOld
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val useCaseKoiModule = module {
    factoryOf(::AuthCheckUseCase) { bind<AuthCheckUseCase>() }
    single<LoginUseCase> { LoginUseCase(get(), get()) }
    single<ReadLocalAuthorizationUseCase> { ReadLocalAuthorizationUseCase(get()) }
    single<SaveLocalAuthorizationUseCase> { SaveLocalAuthorizationUseCase(get()) }
    factoryOf(::LoggedUserUseCase) { bind<LoggedUserUseCase>() }
    factoryOf(::TokenManager) { bind<TokenManager>() }
    single<LocalTokenUseCase> { LocalTokenUseCase(get()) }
    factoryOf(::LogOffUseCase) { bind <LogOffUseCase>() }
    factoryOf(::AddDeviceUseCase) { bind <AddDeviceUseCase>() }
    factoryOf(::UsersUseCase) { bind <UsersUseCase>() }
    single<DeleteParamUseCase> { DeleteParamUseCase(get()) }
    factoryOf(::PermissionsUseCase) { bind <PermissionsUseCase>() }
    factoryOf(::AddUserUseCase) { bind <AddUserUseCase>() }
    single<DeleteUserUseCase> { DeleteUserUseCase(get()) }
    factoryOf(::UpdateUserUseCase) { bind <UpdateUserUseCase>() }
    factoryOf(::DevicesGroupsUseCase) { bind <DevicesGroupsUseCase>() }
    single<UnitsUseCase> { UnitsUseCase(get()) }
    single<AddDevicesGroupUseCase> { AddDevicesGroupUseCase(get()) }
    single<AddUnitUseCase> { AddUnitUseCase(get()) }
    single<DeleteDevicesGroupUseCase> { DeleteDevicesGroupUseCase(get()) }
    single<DeleteUnitUseCase> { DeleteUnitUseCase(get()) }
    single<UpdateDevicesGroupUseCase> { UpdateDevicesGroupUseCase(get()) }
    single<UpdateUnitUseCase> { UpdateUnitUseCase(get()) }
    single<DeviceByIdUseCase> { DeviceByIdUseCase(get(), get()) }
    single<WebSocketUseCaseOld> { WebSocketUseCaseOld(get(), get()) }
    factoryOf(::WebSocketUseCase) { bind <WebSocketUseCase>() }
    single<CloseWebsocketUseCase> { CloseWebsocketUseCase(get()) }
    single<UpdateNotificationsDeviceUseCase> { UpdateNotificationsDeviceUseCase(get()) }
    single<UpdateDeviceUseCase> { UpdateDeviceUseCase(get()) }
    single<UpdateParamUseCase> { UpdateParamUseCase(get()) }
    factoryOf(::UpdateFavoriteUseCase) { bind <UpdateFavoriteUseCase>() }
    factoryOf(::DevicesUseCase) { bind <DevicesUseCase>() }
    single<RemoveFavoriteUseCase> { RemoveFavoriteUseCase(get()) }
    single<DeleteDeviceUseCase> { DeleteDeviceUseCase(get(), get()) }
    single<HistoryUseCase> { HistoryUseCase(get()) }
}
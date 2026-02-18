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
    factoryOf(::LoginUseCase) { bind<LoginUseCase>() }
    factoryOf(::ReadLocalAuthorizationUseCase) { bind<ReadLocalAuthorizationUseCase>() }
    factoryOf(::SaveLocalAuthorizationUseCase) { bind<SaveLocalAuthorizationUseCase>() }
    factoryOf(::LoggedUserUseCase) { bind<LoggedUserUseCase>() }
    factoryOf(::TokenManager) { bind<TokenManager>() }
    single<LocalTokenUseCase> { LocalTokenUseCase(get()) }
    factoryOf(::LogOffUseCase) { bind <LogOffUseCase>() }
    factoryOf(::AddDeviceUseCase) { bind <AddDeviceUseCase>() }
    factoryOf(::UsersUseCase) { bind <UsersUseCase>() }
    single<DeleteParamUseCase> { DeleteParamUseCase(get()) }
    factoryOf(::PermissionsUseCase) { bind <PermissionsUseCase>() }
    factoryOf(::AddUserUseCase) { bind <AddUserUseCase>() }
    factoryOf(::DeleteUserUseCase) { bind <DeleteUserUseCase>() }
    factoryOf(::UpdateUserUseCase) { bind <UpdateUserUseCase>() }
    factoryOf(::DevicesGroupsUseCase) { bind <DevicesGroupsUseCase>() }
    factoryOf(::UnitsUseCase) { bind <UnitsUseCase>() }
    factoryOf(::AddDevicesGroupUseCase) { bind <AddDevicesGroupUseCase>() }
    factoryOf(::AddUnitUseCase) { bind <AddUnitUseCase>() }
    factoryOf(::DeleteDevicesGroupUseCase) { bind <DeleteDevicesGroupUseCase>() }
    factoryOf(::DeleteUnitUseCase) { bind <DeleteUnitUseCase>() }
    factoryOf(::UpdateDevicesGroupUseCase) { bind <UpdateDevicesGroupUseCase>() }
    factoryOf(::UpdateUnitUseCase) { bind <UpdateUnitUseCase>() }
    factoryOf(::DeviceByIdUseCase) { bind <DeviceByIdUseCase>() }
    single<WebSocketUseCaseOld> { WebSocketUseCaseOld(get(), get()) }
    factoryOf(::WebSocketUseCase) { bind <WebSocketUseCase>() }
    factoryOf(::CloseWebsocketUseCase) { bind <CloseWebsocketUseCase>() }
    single<UpdateNotificationsDeviceUseCase> { UpdateNotificationsDeviceUseCase(get()) }
    single<UpdateDeviceUseCase> { UpdateDeviceUseCase(get()) }
    single<UpdateParamUseCase> { UpdateParamUseCase(get()) }
    factoryOf(::UpdateFavoriteUseCase) { bind <UpdateFavoriteUseCase>() }
    factoryOf(::DevicesUseCase) { bind <DevicesUseCase>() }
    single<RemoveFavoriteUseCase> { RemoveFavoriteUseCase(get()) }
    single<DeleteDeviceUseCase> { DeleteDeviceUseCase(get(), get()) }
    factoryOf(::HistoryUseCase) { bind <HistoryUseCase>() }
}
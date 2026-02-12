package com.edurda77.impulsmeteo.di

import com.edurda77.data.repository.DataStoreRepositoryImpl
import com.edurda77.data.repository.DevicesGroupsRepositoryImpl
import com.edurda77.data.repository.DevicesRepositoryImpl
import com.edurda77.data.repository.FavoriteRepositoryImpl
import com.edurda77.data.repository.JwtRepositoryImpl
import com.edurda77.data.repository.LocalRepositoryImpl
import com.edurda77.data.repository.OldRemoteRepositoryImpl
import com.edurda77.data.repository.RemoteRepositoryImpl
import com.edurda77.data.repository.UsersRepositoryImpl
import com.edurda77.data.repository.WebSocketRepositoryImpl
import com.edurda77.data.repository.WebSocketRepositoryOldImpl
import com.edurda77.domain.repository.DataStoreRepository
import com.edurda77.domain.repository.DevicesGroupsRepository
import com.edurda77.domain.repository.DevicesRepository
import com.edurda77.domain.repository.FavoriteRepository
import com.edurda77.domain.repository.JwtRepository
import com.edurda77.domain.repository.LocalRepository
import com.edurda77.domain.repository.OldRemoteRepository
import com.edurda77.domain.repository.RemoteRepository
import com.edurda77.domain.repository.UsersRepository
import com.edurda77.domain.repository.WebSocketRepository
import com.edurda77.domain.repository.WebSocketRepositoryOld
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val repoModule = module {
    single<OldRemoteRepository> { OldRemoteRepositoryImpl(get()) }
    single<WebSocketRepositoryOld> { WebSocketRepositoryOldImpl(get()) }
    singleOf(::DataStoreRepositoryImpl) { bind<DataStoreRepository>() }
    singleOf(::LocalRepositoryImpl) { bind<LocalRepository>() }
    singleOf(::JwtRepositoryImpl) { bind<JwtRepository>() }
    singleOf(::RemoteRepositoryImpl) { bind<RemoteRepository>() }
    singleOf(::DevicesGroupsRepositoryImpl) { bind<DevicesGroupsRepository>() }
    singleOf(::WebSocketRepositoryImpl) { bind<WebSocketRepository>() }
    singleOf(::DevicesRepositoryImpl) { bind<DevicesRepository>() }
    singleOf(::FavoriteRepositoryImpl) { bind<FavoriteRepository>() }
    singleOf(::UsersRepositoryImpl) { bind<UsersRepository>() }

}
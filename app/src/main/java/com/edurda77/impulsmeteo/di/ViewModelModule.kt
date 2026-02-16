package com.edurda77.impulsmeteo.di

import com.edurda77.device_detail.DeviceViewModel
import com.edurda77.devices_list.DevicesViewModel
import com.edurda77.directories.DirectoriesViewModel
import com.edurda77.login_screen.LoginViewModel
import com.edurda77.splash.SplashViewModel
import com.edurda77.users_list.UsersViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        DeviceViewModel(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get (),
        )
    }
    viewModelOf(::DevicesViewModel)
    viewModelOf(::DirectoriesViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::SplashViewModel)
    viewModelOf(::UsersViewModel)
}
package com.edurda77.impulsmeteo

import android.app.Application
import com.edurda77.impulsmeteo.di.baseModule
import com.edurda77.impulsmeteo.di.downloadUpdateModule
import com.edurda77.impulsmeteo.di.repoModule
import com.edurda77.impulsmeteo.di.useCaseKoiModule
import com.edurda77.impulsmeteo.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MateoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@MateoApp)
            modules(
                baseModule, repoModule, useCaseKoiModule, viewModelModule, downloadUpdateModule
            )
        }
    }
}
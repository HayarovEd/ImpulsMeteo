package com.edurda77.impulsmeteo.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.edurda77.domain.utils.APP_PREFERENCES
import com.edurda77.domain.utils.PING_INTERVAL
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.pingInterval
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.net.SocketException
import kotlin.time.DurationUnit
import kotlin.time.toDuration

val baseModule = module {
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { androidContext().preferencesDataStoreFile(APP_PREFERENCES) }
        )
    }
    single<HttpClient> {
        HttpClient(OkHttp) {
            install(HttpTimeout) {
                connectTimeoutMillis = 30000
                requestTimeoutMillis = 60000
                socketTimeoutMillis = 60000
            }
            install(HttpRequestRetry) {
                maxRetries = 3
                retryOnExceptionIf { _, cause ->
                    cause is SocketException || cause is SocketTimeoutException
                }
                delayMillis { retry ->
                    1000L * (1 shl retry)
                }
            }
            install(Logging) {
                logger = Logger.DEFAULT
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(WebSockets) {
                val json = Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                    encodeDefaults = true
                    explicitNulls = false
                }
                pingInterval = PING_INTERVAL.toDuration(DurationUnit.MILLISECONDS)
                contentConverter = KotlinxWebsocketSerializationConverter(json)
            }
        }
    }
}
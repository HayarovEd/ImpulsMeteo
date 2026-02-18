package com.edurda77.resources.model

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
    @Serializable
    data object Login : NavigationRoute()

    @Serializable
    data object Splash : NavigationRoute()

    @Serializable
    data object Devices : NavigationRoute()

    @Serializable
    data object Users : NavigationRoute()

    @Serializable
    data object Directory : NavigationRoute()

    @Serializable
    data class Device(
        val id: String
    ) : NavigationRoute()
}
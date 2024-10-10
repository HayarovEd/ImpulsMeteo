package com.edurda77.domain.model

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
    /* @Serializable
    data class Camera(
         val id:String
     ): NavigationRoute()*/
}
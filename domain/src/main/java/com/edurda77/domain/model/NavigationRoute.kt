package com.edurda77.domain.model

import kotlinx.serialization.Serializable

sealed class NavigationRoute {
  @Serializable
    data object Login: NavigationRoute()
    /*@Serializable
    data object ListCameras: NavigationRoute()*/
    @Serializable
    data object Splash: NavigationRoute()
   /* @Serializable
   data class Camera(
        val id:String
    ): NavigationRoute()*/
}
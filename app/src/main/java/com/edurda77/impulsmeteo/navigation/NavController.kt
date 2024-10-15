package com.edurda77.impulsmeteo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edurda77.devices_list.DevicesScreen
import com.edurda77.domain.model.NavigationRoute
import com.edurda77.login_screen.LoginScreen
import com.edurda77.resources.uikit.UiBottomNavigation
import com.edurda77.splash.SplashScreen


@Composable
fun NavController(
    startDestination: NavigationRoute = NavigationRoute.Splash,
) {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    NavHost(navController = navController, startDestination = startDestination) {

        composable<NavigationRoute.Splash> {
            SplashScreen(
                onGoToListCameras = {
                    navController.navigate(NavigationRoute.Devices)
                },
                onGoToLogin = {
                    navController.navigate(NavigationRoute.Login)
                }
            )
        }
        composable<NavigationRoute.Login> {
            LoginScreen(
                onGoToListDevices = {
                    navController.navigate(NavigationRoute.Devices)
                },
                configuration = configuration
            )
        }
        composable<NavigationRoute.Devices> {
            DevicesScreen(
                configuration = configuration,
                onGoToDevice = {

                },
                onGoToLogin = {
                    navController.navigate(NavigationRoute.Login)
                },
                bottomBarContent = {
                    UiBottomNavigation(
                        navController = navController
                    )
                }
            )
        }
        /*
        composable<NavigationRoute.Camera> {
            CameraScreen(
                configuration = configuration
            )
        }
        composable<NavigationRoute.ListCameras> {
            MonitorsScreen(
                configuration = configuration,
                onGoToLogin = {
                    navController.navigate(NavigationRoute.Login)
                },
                onGoToCamera = { id ->
                    navController.navigate(NavigationRoute.Camera(id))
                },
            )
        }*/
    }
}
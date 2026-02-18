package com.edurda77.impulsmeteo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.edurda77.device_detail.DeviceScreen
import com.edurda77.devices_list.DevicesScreenRoot
import com.edurda77.directories.DirectoriesScreenRoot
import com.edurda77.resources.model.NavigationRoute
import com.edurda77.login_screen.LoginScreen
import com.edurda77.resources.uikit.UiBottomNavigation
import com.edurda77.splash.SplashScreenRoot
import com.edurda77.users_list.UsersListScreenRoot


@Composable
fun NavController(
    startDestination: NavigationRoute = NavigationRoute.Splash,
) {
    val navController = rememberNavController()
    val configuration = LocalConfiguration.current
    NavHost(navController = navController, startDestination = startDestination) {

        composable<NavigationRoute.Splash> {
            SplashScreenRoot(
                configuration = configuration,
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
            DevicesScreenRoot(
                configuration = configuration,
                onGoToDevice = {
                    navController.navigate(NavigationRoute.Device(it))
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
        composable<NavigationRoute.Users> {
            UsersListScreenRoot(
                configuration = configuration,
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
        composable<NavigationRoute.Directory> {
            DirectoriesScreenRoot(
                configuration = configuration,
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

        composable<NavigationRoute.Device> {
            DeviceScreen(
                configuration = configuration,
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
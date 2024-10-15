package com.edurda77.login_screen

import android.content.res.Configuration
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun LoginScreen(
    onGoToListDevices: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
    configuration: Configuration
) {
    val state = viewModel.state.collectAsState()
    val onEvent = viewModel::onEvent
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiLoginEvents.SnackbarEvent -> {
                    if (event.message != null) {
                        snakeBarHostState.showSnackbar(
                            message = event.message.asString(context),
                            duration = SnackbarDuration.Short
                        )
                    }
                }

                is UiLoginEvents.NavigateEvent -> {
                    onGoToListDevices()
                }
            }
        }
    }
    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            LandscapeLoginScreen(
                snakeBarHostState = snakeBarHostState,
                isLoading = state.value.isLoading,
                email = state.value.email,
                password = state.value.password,
                onEvent = onEvent
            )
        }

        else -> {
            PortraitLoginScreen(
                snakeBarHostState = snakeBarHostState,
                isLoading = state.value.isLoading,
                email = state.value.email,
                password = state.value.password,
                onEvent = onEvent
            )
        }
    }
}
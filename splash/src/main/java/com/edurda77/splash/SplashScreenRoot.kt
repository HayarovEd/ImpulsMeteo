package com.edurda77.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreenRoot(
    onGoToLogin: () -> Unit,
    onGoToListCameras: () -> Unit,
    viewModel: SplashViewModel = koinViewModel()
) {
    val state = viewModel.state.collectAsState()
    SplashScreen(
        state = state.value,
        onGoToLogin = onGoToLogin,
        onGoToListCameras = onGoToListCameras
    )
}

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    state: SplashScreenState,
    onGoToLogin: () -> Unit,
    onGoToListCameras: () -> Unit,
) {
    LaunchedEffect(key1 = state) {
        when (state) {
            SplashScreenState.LOADING -> {}

            SplashScreenState.AUTHORIZED -> {
                onGoToListCameras()
            }

            SplashScreenState.NOT_AUTHORIZED -> {
                onGoToLogin()
            }
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Image(
            modifier = modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.meteo_bk),
            contentDescription = "",
            contentScale = ContentScale.FillHeight
        )
        LinearProgressIndicator(
            modifier = modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.6f)
        )
    }
}

@Preview
@Composable
private fun SplashScreenView1() {
    ImpulsMeteoTheme {
        SplashScreen(
            state = SplashScreenState.LOADING,
            onGoToLogin = {},
            onGoToListCameras = {}
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SplashScreenView2() {
    ImpulsMeteoTheme {
        SplashScreen(
            state = SplashScreenState.LOADING,
            onGoToLogin = {},
            onGoToListCameras = {}
        )
    }
}

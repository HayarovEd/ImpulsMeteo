package com.edurda77.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.edurda77.resources.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onGoToLogin: () -> Unit,
    onGoToListCameras: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    LaunchedEffect(key1 = state.value) {
        when (state.value) {
            SplashScreenState.LOADING -> {}

            SplashScreenState.AUTHORIZED -> {
                onGoToListCameras()
            }

            SplashScreenState.NOT_AUTHORIZED -> {
                onGoToLogin()
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = modifier
                .fillMaxHeight(0.4f),
            painter = painterResource(id = R.drawable.favicon_3),
            contentDescription = "",
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = modifier.height(10.dp))
        LinearProgressIndicator(
            modifier = modifier
                .fillMaxWidth(0.6f)
        )
    }
}

@Preview
@Composable
private fun Sample() {
    SplashScreen(
        onGoToLogin = {},
        onGoToListCameras = {}
    )
}

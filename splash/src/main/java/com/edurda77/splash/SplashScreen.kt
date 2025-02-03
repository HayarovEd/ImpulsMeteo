package com.edurda77.splash

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    onGoToLogin: () -> Unit,
    onGoToListCameras: () -> Unit,
    configuration: Configuration,
    viewModel: SplashViewModel = koinViewModel()
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
    val backgroundBrush = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.background,
            MaterialTheme.colorScheme.tertiaryContainer
        )
    )
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = backgroundBrush),
    ) {
        Column(
            modifier = modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = modifier,
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
}

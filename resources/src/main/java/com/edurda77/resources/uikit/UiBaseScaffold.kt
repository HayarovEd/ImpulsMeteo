package com.edurda77.resources.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

@Composable
fun UiBaseScaffold(
    modifier: Modifier = Modifier,
    message: UiText?,
    topBarContent: @Composable () -> Unit = {},
    bottomBarContent: @Composable () -> Unit = {},
    fabContent: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val snakeBarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    LaunchedEffect(key1 = message) {
        if (message != null) {
            snakeBarHostState.showSnackbar(
                message = message.asString(context),
                duration = SnackbarDuration.Short
            )
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
            .background(brush = backgroundBrush)
    ) {
        /* AsyncImage(
             modifier = imageModifier,
             model = if (isSystemInDarkTheme()) R.drawable.night_cloud else
                 R.drawable.cloud,
             contentDescription = "",
             contentScale = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ContentScale.FillWidth else ContentScale.FillHeight
         )*/
        Scaffold(
            modifier = modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(snakeBarHostState) },
            topBar = topBarContent,
            bottomBar = bottomBarContent,
            floatingActionButton = fabContent
        ) { paddings ->
            content(paddings)
        }
    }
}
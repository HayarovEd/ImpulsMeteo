package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.edurda77.resources.R

@Composable
fun UiBaseScaffold(
    modifier: Modifier = Modifier,
    message: UiText?,
    configuration: Configuration,
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
    val imageModifier =
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) modifier.fillMaxWidth() else modifier.fillMaxHeight()
    Box(modifier = modifier.fillMaxSize()) {
        Image(
            modifier = imageModifier,
            painter = if (isSystemInDarkTheme()) painterResource(R.drawable.night_cloud) else painterResource(
                R.drawable.cloud
            ),
            contentDescription = "",
            contentScale = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) ContentScale.FillWidth else ContentScale.FillHeight
        )
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
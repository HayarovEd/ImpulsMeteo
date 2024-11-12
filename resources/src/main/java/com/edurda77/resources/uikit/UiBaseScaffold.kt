package com.edurda77.resources.uikit

import android.content.res.Configuration
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
import coil3.compose.AsyncImage
import com.edurda77.resources.R

@Composable
fun UiBaseScaffold(
    modifier: Modifier = Modifier,
    message: UiText?,
    configuration: Configuration,
    image: Any = if (isSystemInDarkTheme()) R.drawable.night_cloud else
        R.drawable.cloud,
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
        AsyncImage(
            modifier = imageModifier,
            model = image,
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
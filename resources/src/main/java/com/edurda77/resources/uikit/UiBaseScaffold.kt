package com.edurda77.resources.uikit

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun UiBaseScaffold(
    modifier: Modifier = Modifier,
    message: UiText?,
    topBarContent: @Composable () -> Unit = {},
    bottomBarContent: @Composable () -> Unit = {},
    fabContent: @Composable () -> Unit = {},
    snakeBarHostState: SnackbarHostState = SnackbarHostState(),
    content: @Composable (PaddingValues) -> Unit
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snakeBarHostState) },
        topBar = topBarContent,
        bottomBar = bottomBarContent,
        floatingActionButton = fabContent
    ) { paddings ->
        content(paddings)
    }
}
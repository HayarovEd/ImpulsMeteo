package com.edurda77.resources.uikit

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography

@Composable
fun NoAccess(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.no_access_this)
) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = modifier
                .fillMaxWidth(),
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = Typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    ImpulsMeteoTheme {
        NoAccess(
            title = stringResource(R.string.no_access_this)
        )
    }
}
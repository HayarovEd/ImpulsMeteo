package com.edurda77.resources.uikit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Device
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography

@Composable
fun ItemDevice(
    modifier: Modifier = Modifier,
    device: Device
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        border = BorderStroke(
            width = 2.dp,
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
        )
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                // .clickable(onClick = onClick)
                .padding(10.dp),
        ) {
            Text(
                modifier = modifier.align(alignment = Alignment.TopEnd),
                text = if (device.status) stringResource(R.string.online) else stringResource(R.string.offline),
                color = if (device.status) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                style = Typography.bodyLarge,
            )
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    // .clickable(onClick = onClick)
                    .padding(10.dp),
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = device.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = modifier.height(5.dp))
                device.params.forEach { param ->
                    UiRowDeviceValue(
                        icon = param.idUnit.asUiIconParam(),
                        value = param.value,
                        unit = param.idUnit.asUiTextParam(),
                        name = param.label
                    )
                    Spacer(modifier = modifier.height(3.dp))
                }
            }
        }
    }
}
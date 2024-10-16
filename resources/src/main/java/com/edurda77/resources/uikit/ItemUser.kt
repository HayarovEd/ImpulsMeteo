package com.edurda77.resources.uikit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.User
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography

@Composable
fun ItemUser(
    modifier: Modifier = Modifier,
    user: User
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            // .clickable(onClick = onClick)
            .padding(10.dp),
    ) {
        Text(
            modifier = modifier.fillMaxWidth(),
            text = user.name,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        Text(
            modifier = modifier.fillMaxWidth(),
            text = user.email,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(2.dp))
        HorizontalDivider(
            modifier = modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(2.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = stringResource(R.string.access),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.bodyLarge,
            )
            Spacer(modifier = modifier.width(10.dp))
            Column(modifier = modifier.weight(2f)) {
                user.permissions.forEach {
                    Text(
                        modifier = modifier,
                        text = it.displayName,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.labelSmall,
                    )
                    Spacer(modifier = modifier.height(2.dp))
                }
            }
        }
        HorizontalDivider(
            modifier = modifier.fillMaxWidth(),
            thickness = 2.dp,
            color = MaterialTheme.colorScheme.tertiary
        )
        Spacer(modifier = modifier.height(2.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = modifier.weight(1f),
                text = stringResource(R.string.devices),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.bodyLarge,
            )
            Spacer(modifier = modifier.width(10.dp))
            Column(modifier = modifier.weight(2f)) {
                user.devices.forEach {
                    Text(
                        modifier = modifier,
                        text = it.name,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.labelSmall,
                    )
                    Spacer(modifier = modifier.height(2.dp))
                }
            }
        }
    }
}
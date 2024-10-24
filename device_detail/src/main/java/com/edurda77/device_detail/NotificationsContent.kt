package com.edurda77.device_detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Notifications
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography

@Composable
fun NotificationsContent(
    modifier: Modifier = Modifier,
    onClickChangeVisibleBottomSheet: () -> Unit,
    name: String?,
    notifications: Notifications?,
) {
    val currentNifications = remember { mutableStateOf(notifications) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = modifier,
                text = stringResource(R.string.edit_notification_of_device),
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = Typography.bodyLarge,
            )
            IconButton(onClick = onClickChangeVisibleBottomSheet) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_close_24),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
            }
        }
        Spacer(modifier = modifier.height(10.dp))
        Card(
            modifier = modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            ),
            colors = CardDefaults.cardColors(
                containerColor = Color.Transparent
            )
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(10.dp),
            ) {
                Text(
                    modifier = modifier,
                    text = name ?: "",
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(10.dp))
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = {
                                currentNifications.value = currentNifications.value?.copy(
                                    currentNifications.value?.deviceStatus != true
                                )
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = currentNifications.value?.deviceStatus == true,
                        onCheckedChange = {
                            currentNifications.value = currentNifications.value?.copy(
                                currentNifications.value?.deviceStatus != true
                            )
                        }
                    )
                    Spacer(modifier = modifier.width(5.dp))
                    Text(
                        modifier = modifier,
                        text = stringResource(R.string.notificate_to_change_status),
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        style = Typography.labelSmall,
                    )
                }
            }
        }
    }
}
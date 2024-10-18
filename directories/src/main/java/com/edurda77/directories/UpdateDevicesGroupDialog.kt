package com.edurda77.directories

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@Composable
fun UpdateDevicesGroupDialog(
    modifier: Modifier = Modifier,
    currentName: String,
    onCloseClick: () -> Unit,
    onUpdateClick: (String) -> Unit,
) {
    val name = remember { mutableStateOf(currentName) }

    Column {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.add),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = name.value,
            label = stringResource(id = R.string.title),
            onClickContent = {
                name.value = it
            }
        )
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                modifier = modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = onCloseClick
            ) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = modifier.width(10.dp))
            Button(
                modifier = modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                onClick = {
                    onCloseClick()
                    onUpdateClick(
                        name.value,
                    )
                }
            ) {
                Text(
                    text = stringResource(id = R.string.ok),
                    style = Typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primaryContainer
                )
            }
        }
    }
}
package com.edurda77.directories

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
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

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ){
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.update_group),
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
        Spacer(modifier = modifier.height(5.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent
                ),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                onClick = onCloseClick
            ) {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(id = R.string.cancel),
                    style = Typography.bodySmall,
                )
            }
            Spacer(modifier = modifier.width(10.dp))
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = {
                    onCloseClick()
                    onUpdateClick(
                        name.value,
                    )
                }
            ) {
                Text(
                    color = MaterialTheme.colorScheme.background,
                    text = stringResource(id = R.string.ok),
                    style = Typography.bodySmall,
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UpdateDevicesGroupDialogView1() {
    ImpulsMeteoTheme {
        UpdateDevicesGroupDialog(
            currentName = "Hallow",
            onUpdateClick = {},
            onCloseClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun UpdateDevicesGroupDialog2() {
    ImpulsMeteoTheme {
        UpdateDevicesGroupDialog(
            currentName = "Hallow",
            onUpdateClick = {},
            onCloseClick = {}
        )
    }
}
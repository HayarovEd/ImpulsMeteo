package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography

@Composable
fun UiAlertDialog(
    title: String,
    onClickConfirm: () -> Unit,
    onClickCancel: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Text(
                modifier = modifier
                    .padding(10.dp),
                text = title,
                color = MaterialTheme.colorScheme.onBackground,
                style = Typography.bodyLarge,
            )
        },
        onDismissRequest = onClickCancel,
        dismissButton = {
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
                onClick = onClickCancel
            ) {
                Text(
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(id = R.string.cancel),
                    style = Typography.bodySmall,
                )
            }
        },
        confirmButton = {
            Button(
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                shape = MaterialTheme.shapes.medium,
                onClick = onClickConfirm
            ) {
                Text(
                    color = MaterialTheme.colorScheme.background,
                    text = stringResource(id = R.string.ok),
                    style = Typography.bodySmall,
                )
            }
        },
        shape = MaterialTheme.shapes.medium
    )
}


@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiAlertDialogView1() {
    ImpulsMeteoTheme {
        UiAlertDialog(
            title = "hallo",
            onClickCancel = {},
            onClickConfirm = {},
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun UiAlertDialogdView2() {
    ImpulsMeteoTheme {
        UiAlertDialog(
            title = "hallo",
            onClickCancel = {},
            onClickConfirm = {},
        )
    }
}
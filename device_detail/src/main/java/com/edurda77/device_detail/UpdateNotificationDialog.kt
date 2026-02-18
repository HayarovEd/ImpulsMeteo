package com.edurda77.device_detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.NotificationDeviceOld
import com.edurda77.domain.model.ParamOld
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField

@Composable
fun UpdateNotificationDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    notificationDeviceOld: NotificationDeviceOld,
    onConfirmClick: (Int, Int, String, Int) -> Unit,
    params: List<ParamOld>,
) {
    val condition = remember { mutableStateOf(notificationDeviceOld.condition) }
    val value = remember { mutableStateOf(notificationDeviceOld.value.toString()) }
    val parameterId = remember { mutableIntStateOf(NEGATIVE_ID) }
    val parameter =
        remember { mutableStateOf(params.first { it.id == notificationDeviceOld.idParam }.label) }
    val expandedParameters = remember { mutableStateOf(false) }
    val expandedConditions = remember { mutableStateOf(false) }
    val conditions = listOf("<=", ">=")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.weight(1.5f)
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.parameter),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(3.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = modifier.basicMarquee(),
                        text = parameter.value,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.bodyLarge,
                    )
                    DropdownMenu(
                        modifier = modifier,
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = expandedParameters.value,
                        onDismissRequest = { expandedParameters.value = false }
                    ) {
                        params.forEach { param ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = param.label,
                                        style = Typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }, onClick = {
                                    parameter.value = param.label
                                    parameterId.intValue = param.id
                                })
                        }
                    }
                    UiIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {
                            expandedParameters.value = true
                        }
                    )
                }
            }
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.conditions),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = modifier,
                        text = condition.value,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.bodyLarge,
                    )
                    DropdownMenu(
                        modifier = modifier,
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = expandedConditions.value,
                        onDismissRequest = { expandedConditions.value = false }
                    ) {
                        conditions.forEach { cnd ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = cnd,
                                        style = Typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }, onClick = {
                                    condition.value = cnd
                                })
                        }
                    }
                    UiIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = {
                            expandedConditions.value = true
                        }
                    )
                }
            }
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.value),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(3.dp))
                UiTextField(
                    content = value.value,
                    label = "",
                    isOnlyDigit = true,
                    onClickContent = {
                        value.value = it
                    }
                )
            }
        }
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
                    onConfirmClick(
                        notificationDeviceOld.id ?: NEGATIVE_ID,
                        parameterId.intValue,
                        condition.value,
                        value.value.toIntOrNull() ?: 0
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
    showBackground = true
)
@Composable
private fun UpdateNotificationDialogView() {
    val params = remember {
        (0..5).map {
            ParamOld(
                id = it,
                idUnit = it,
                name = "Param $it",
                label = "label",
                value = it + 5.0,
                color = "#50e3c2",
                classIcon = "wi wi-thermometer",
                isHidden = false,
                idDevice = 1
            )
        }
    }
    ImpulsMeteoTheme {
        UpdateNotificationDialog(
            onCloseClick = {},
            notificationDeviceOld = NotificationDeviceOld(
                condition = "nt1",
                idParam = 1,
                value = 3
            ),
            onConfirmClick = { _, _, _, _ -> },
            params = params
        )
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UpdateNotificationDialogView2() {
    val params = remember {
        (0..5).map {
            ParamOld(
                id = it,
                idUnit = it,
                name = "Param $it",
                label = "label",
                value = it + 5.0,
                color = "#50e3c2",
                classIcon = "wi wi-thermometer",
                isHidden = false,
                idDevice = 1
            )
        }
    }
    ImpulsMeteoTheme {
        UpdateNotificationDialog(
            onCloseClick = {},
            notificationDeviceOld = NotificationDeviceOld(
                condition = "nt1",
                idParam = 1,
                value = 3
            ),
            onConfirmClick = { _, _, _, _ -> },
            params = params
        )
    }
}

package com.edurda77.device_detail

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.ParamOld
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField

@Composable
fun AddNotificationDialog(
    modifier: Modifier = Modifier,
    onCloseClick: () -> Unit,
    onConfirmClick: (Int, String, Int) -> Unit,
    params: List<ParamOld>,
) {

    val condition = remember { mutableStateOf("") }
    val value = remember { mutableStateOf("") }
    val parameter = remember { mutableStateOf("") }
    val parameterId = remember { mutableIntStateOf(NEGATIVE_ID) }
    val expandedParameters = remember { mutableStateOf(false) }
    val expandedConditions = remember { mutableStateOf(false) }
    val conditions = listOf("<=", ">=")

    Column {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier.weight(1f)
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.parameter),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.bodyLarge,
                )
                Spacer(modifier = modifier.height(3.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        modifier = modifier.basicMarquee(),
                        text = parameter.value,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.bodyLarge,
                    )
                    DropdownMenu(
                        modifier = modifier,
                        //offset = DpOffset(x = offsetXDropDownMenu.value, y = 0.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        expanded = expandedParameters.value,
                        onDismissRequest = { expandedParameters.value = false }
                    ) {
                        params.forEach { param ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = param.label,
                                        style = Typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }, onClick = {
                                    parameter.value = param.label
                                    parameterId.intValue = param.id
                                })
                        }
                    }
                    UiIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
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
                        //offset = DpOffset(x = offsetXDropDownMenu.value, y = 0.dp),
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        expanded = expandedConditions.value,
                        onDismissRequest = { expandedConditions.value = false }
                    ) {
                        conditions.forEach { cnd ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = cnd,
                                        style = Typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                }, onClick = {
                                    condition.value = cnd
                                })
                        }
                    }
                    UiIconButton(
                        icon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
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
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = onCloseClick
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.cancel),
                    color = MaterialTheme.colorScheme.error,
                    style = Typography.bodyLarge,
                )
            }
            TextButton(
                onClick = {
                    onConfirmClick(
                        parameterId.intValue,
                        condition.value,
                        value.value.toIntOrNull() ?: 0
                    )
                }
            ) {
                Text(
                    modifier = modifier,
                    text = stringResource(id = R.string.ok),
                    color = MaterialTheme.colorScheme.primary,
                    style = Typography.bodyLarge,
                )
            }
        }
    }
}

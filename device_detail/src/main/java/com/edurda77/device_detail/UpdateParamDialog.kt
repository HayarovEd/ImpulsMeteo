package com.edurda77.device_detail

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.UnitMeteoOld
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiTextField

@Composable
fun UpdateParamDialog(
    modifier: Modifier = Modifier,
    param: Param,
    units: List<UnitMeteoOld>,
    onCloseClick: () -> Unit,
    onUpdateClick: (Param) -> Unit,
) {
    val label = remember { mutableStateOf(param.label) }
    val selectedUnit =
        remember { mutableStateOf(units.firstOrNull { it.id == param.idUnit }?.name ?: "") }
    val isHidden = remember { mutableStateOf(param.isHidden) }
    val expandedUnitsMenu = remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(10.dp)
    ) {
        Text(
            modifier = modifier,
            text = stringResource(id = R.string.update_param),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.bodyLarge,
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = label.value,
            label = stringResource(id = R.string.title),
            onClickContent = {
                label.value = it
            }
        )
        Spacer(modifier = modifier.height(5.dp))
        UiTextField(
            content = selectedUnit.value,
            label = stringResource(id = R.string.units_lower_case),
            onClickContent = {},
            trailingIcon = if (expandedUnitsMenu.value) ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24)
            else ImageVector.vectorResource(
                id = R.drawable.baseline_arrow_drop_up_24
            ),
            readOnly = true,
            onClickTrailingIcon = {
                expandedUnitsMenu.value = true
            },
            maxLines = 1
        )
        DropdownMenu(
            expanded = expandedUnitsMenu.value,
            onDismissRequest = {
                expandedUnitsMenu.value = false
            }) {
            units.forEach {
                DropdownMenuItem(
                    text = {
                        Text(
                            modifier = modifier
                                .background(if (units.firstOrNull { it.name == selectedUnit.value } != null) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)
                                .padding(4.dp),
                            text = it.name,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.labelSmall,
                        )
                    },
                    onClick = {
                        selectedUnit.value = it.name
                    },
                )
            }
        }
        Spacer(modifier = modifier.height(5.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Switch(
                checked = isHidden.value,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.White
                ),
                onCheckedChange = {
                    isHidden.value = !isHidden.value
                }
            )
            Spacer(modifier = modifier.width(5.dp))
            Text(
                modifier = modifier,
                text = stringResource(id = R.string.hite_parameter),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = Typography.bodyLarge,
            )
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
                enabled = selectedUnit.value.isNotBlank(),
                onClick = {
                    onCloseClick()
                    onUpdateClick(
                        param.copy(
                            label = label.value,
                            isHidden = isHidden.value,
                            idUnit = units.firstOrNull { it.name == selectedUnit.value }?.id ?: 0
                        )
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
private fun UpdateParamDialogView1() {
    val units = remember { (0..5).map {
        UnitMeteoOld(
            id = it,
            name = "Param $it",
            short = "prm$it"
        )
    } }
    ImpulsMeteoTheme {
        UpdateParamDialog(
            param = Param(
                id = 0,
                idUnit = 0,
                name = "Param 0",
                label = "label",
                value = 0+5.0,
                color = "#50e3c2",
                classIcon = "wi wi-thermometer",
                isHidden = false,
                idDevice = 1
            ),
            units = units,
            onUpdateClick = {},
            onCloseClick = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun UpdateParamDialog2() {
    val units = remember { (0..5).map {
        UnitMeteoOld(
            id = it,
            name = "Param $it",
            short = "prm$it"
        )
    } }
    ImpulsMeteoTheme {
        UpdateParamDialog(
            param = Param(
                id = 0,
                idUnit = 0,
                name = "Param 0",
                label = "label",
                value = 0+5.0,
                color = "#50e3c2",
                classIcon = "wi wi-thermometer",
                isHidden = true,
                idDevice = 1
            ),
            units = units,
            onUpdateClick = {},
            onCloseClick = {}
        )
    }
}
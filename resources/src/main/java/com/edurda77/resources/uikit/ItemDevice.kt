package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.Param
import com.edurda77.domain.utils.TEMPERATURE_ID
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography

@Composable
fun ItemDevice(
    modifier: Modifier = Modifier,
    device: Device,
    configuration: Configuration,
    onClickDevice: () -> Unit,
    onClickChangeFavorite: () -> Unit,
    onDeleteClick: (Int) -> Unit,
) {

    val expandedDeleteDialog = remember { mutableStateOf(false) }

    if (expandedDeleteDialog.value) {
        UiAlertDialog(
            title = stringResource(R.string.sure_delete_device),
            onClickConfirm = {
                onDeleteClick(device.id)
                expandedDeleteDialog.value = false
            },
            onClickCancel = {
                expandedDeleteDialog.value = false
            }
        )
    }
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        /* elevation = CardDefaults.cardElevation(
             defaultElevation = 10.dp
         ),*/
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        ),
        onClick = onClickDevice
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(10.dp),
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = modifier
                        .clip(shape = RoundedCornerShape(100.dp))
                        .background(if (device.status) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.error)
                        .padding(10.dp),
                ) {
                    Icon(
                        painter = if (device.status) painterResource(R.drawable.checkmark) else painterResource(
                            R.drawable.cross
                        ),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
                Spacer(modifier = modifier.width(10.dp))
                Column {
                    Text(
                        modifier = modifier,
                        text = device.name,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = modifier.width(5.dp))
                    Text(
                        modifier = modifier,
                        text = device.updatedAt,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = modifier.weight(1f))
                UiIconButton(
                    icon = if (device.isFavorite) ImageVector.vectorResource(R.drawable.baseline_star_24) else ImageVector.vectorResource(
                        R.drawable.baseline_star_border_24
                    ),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    onClick = onClickChangeFavorite,
                )
            }
            Spacer(modifier = modifier.height(10.dp))
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                val steps = if (device.params.size <= 6) device.params.size else 6
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        for (i in 0..<steps step 3) {
                            UiRowDeviceValue(
                                image = device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label,
                                hexColor = device.params[i].color,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                    Spacer(modifier = modifier.width(5.dp))
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        for (i in 1..<steps step 3) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label,
                                hexColor = device.params[i].color,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                    Spacer(modifier = modifier.width(5.dp))
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        for (i in 2..<steps step 3) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label,
                                hexColor = device.params[i].color,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                } else {
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        for (i in 0..<steps step 2) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label,
                                hexColor = device.params[i].color,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                    Spacer(modifier = modifier.width(10.dp))
                    Column(
                        modifier = modifier.weight(1f)
                    ) {
                        for (i in 1..<steps step 2) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label,
                                hexColor = device.params[i].color,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                }
            }
        }
        /*Box(

        ) {
            Column(
                modifier = modifier.align(alignment = Alignment.TopEnd),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = if (device.status) stringResource(R.string.online) else stringResource(R.string.offline),
                    color = if (device.status) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                    style = Typography.bodyLarge,
                )
                UiIconButton(
                    icon = if (device.isFavorite) ImageVector.vectorResource(R.drawable.baseline_star_24) else ImageVector.vectorResource(
                        R.drawable.baseline_star_border_24
                    ),
                    onClick = onClickChangeFavorite
                )
                if (isEnabledDelete) {
                    UiIconButton(
                        icon = ImageVector.vectorResource(
                            R.drawable.baseline_delete_24
                        ),
                        onClick = { expandedDeleteDialog.value = true }
                    )
                }
            }
            Column(
                modifier = modifier
                    .fillMaxWidth()
                // .clickable(onClick = onClick)
                //.padding(10.dp),
            ) {
                Text(
                    modifier = modifier.fillMaxWidth(),
                    text = device.name,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = Typography.titleLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = modifier.height(5.dp))
                val steps = if (device.params.size <= 6) device.params.size else 6

                if (device.params.size > 6) {
                    Spacer(modifier = modifier.height(5.dp))
                    Row(
                        modifier = modifier.horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 6..<device.params.size) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label,
                                hexColor = device.params[i].color,
                            )
                            Spacer(modifier = modifier.width(3.dp))
                        }
                    }
                }
            }
        }*/
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ItemDeviceView1() {
    ImpulsMeteoTheme {
        ItemDevice(
            device = Device(
                id = 0,
                name = "Auto",
                key = "1223",
                status = false,
                video = null,
                updatedAt = "12-03-2025",
                groups = listOf(
                    GroupDevices(
                        id = 1,
                        name = "Perm"
                    )
                ),
                params = listOf(
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    )
                ),
                isFavorite = false
            ),
            configuration = LocalConfiguration.current,
            onClickDevice = {},
            onDeleteClick = {},
            onClickChangeFavorite = {}
        )
    }
}

@Preview(
)
@Composable
private fun ItemDeviceView2() {
    ImpulsMeteoTheme {
        ItemDevice(
            device = Device(
                id = 0,
                name = "Auto",
                key = "1223",
                status = true,
                video = null,
                updatedAt = "12-03-2025",
                groups = listOf(
                    GroupDevices(
                        id = 1,
                        name = "Perm"
                    )
                ),
                params = listOf(
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    ),
                    Param(
                        id = 0,
                        idUnit = 1,
                        name = "Temp",
                        label = "tmp",
                        value = 12.0,
                        color = "#808080",
                        classIcon = "wi wi-thermometer-exterior",
                        isHidden = false,
                        idDevice = 0
                    )
                ),
                isFavorite = true
            ),
            configuration = LocalConfiguration.current,
            onClickDevice = {},
            onDeleteClick = {},
            onClickChangeFavorite = {}
        )
    }
}
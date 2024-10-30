package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Device
import com.edurda77.domain.utils.TEMPERATURE_ID
import com.edurda77.resources.R
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

    val gradient = listOf(
        MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f),
        MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f)
    )

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
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(10.dp))
            .fillMaxWidth()
            /*  .border(
                  width = 2.dp,
                  shape = RoundedCornerShape(10.dp),
                  color = MaterialTheme.colorScheme.onPrimaryContainer
              )*/
            .background(
                brush = Brush.linearGradient(
                    gradient,
                )
            )
            .clickable(onClick = onClickDevice)
            .padding(10.dp),
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
            UiIconButton(
                icon = ImageVector.vectorResource(
                    R.drawable.baseline_delete_24
                ),
                onClick = { expandedDeleteDialog.value = true }
            )
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
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    Column {
                        for (i in 0..<steps step 3) {
                            UiRowDeviceValue(
                                image = device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                    Spacer(modifier = modifier.width(5.dp))
                    Column {
                        for (i in 1..<steps step 3) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                    Spacer(modifier = modifier.width(5.dp))
                    Column {
                        for (i in 2..<steps step 3) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                } else {
                    Column {
                        for (i in 0..<steps step 2) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                    Spacer(modifier = modifier.width(10.dp))
                    Column {
                        for (i in 1..<steps step 2) {
                            UiRowDeviceValue(
                                image = if (device.params[i].idUnit == TEMPERATURE_ID && device.params[i].value >= 0.0) device.params[i].idUnit.asUiImageParam(
                                    true
                                ) else device.params[i].idUnit.asUiImageParam(),
                                value = device.params[i].value,
                                unit = device.params[i].idUnit.asUiTextParam(),
                                name = device.params[i].label
                            )
                            Spacer(modifier = modifier.height(3.dp))
                        }
                    }
                }
            }
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
                            name = device.params[i].label
                        )
                        Spacer(modifier = modifier.width(3.dp))
                    }
                }
            }
        }
    }
}
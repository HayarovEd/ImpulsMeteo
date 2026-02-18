package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.Favorite
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.MeasurementUnit
import com.edurda77.domain.model.newModels.NotificationDevice
import com.edurda77.domain.model.newModels.Param
import com.edurda77.domain.utils.TEMPERATURE_ABB
import com.edurda77.domain.utils.formatDateTime
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.random.Random
import kotlin.time.Clock

@Composable
fun ItemDevice(
    modifier: Modifier = Modifier,
    device: Device,
    favorites: List<Favorite>,
    isEnableUpdate: Boolean,
    configuration: Configuration,
    onClickDevice: () -> Unit,
    onClickChangeFavorite: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
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
                        text = formatDateTime(device.updatedDate),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = Typography.labelSmall,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = modifier.weight(1f))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (device.notificationDevice != null && device.notificationDevice!!.value) ImageVector.vectorResource(
                            R.drawable.bell
                        ) else ImageVector.vectorResource(
                            R.drawable.alert_bell_disable
                        ),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    )

                    UiIconButton(
                        icon = if (device.id in favorites.map { it.deviceId }) ImageVector.vectorResource(
                            R.drawable.baseline_star_24
                        ) else ImageVector.vectorResource(
                            R.drawable.baseline_star_border_24
                        ),
                        enabled = isEnableUpdate,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        onClick = onClickChangeFavorite,
                    )
                }
            }
            val step =
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 3 else 2

            FlowRow(
                modifier = modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(15.dp),
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                maxItemsInEachRow = step
            ) {
                device.params.forEach { param ->
                    UiRowDeviceValue(
                        modifier = modifier.weight(1f),
                        image = if (param.measurementUnit.abbreviation == TEMPERATURE_ABB && param.value >= 0.0) param.measurementUnit.asUiImageParam(
                            true
                        ) else param.measurementUnit.asUiImageParam(),
                        value = param.value,
                        unit = param.measurementUnit.asUiTextParam(),
                        name = param.label,
                        hexColor = param.color,
                    )
                }
                val def = device.params.size % step
                if (def != 0) {
                    (1..(step - def)).forEach { _ ->
                        Spacer(modifier = modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ItemDeviceView1() {
    val params = (1..21).map {
        Param(
            id = "$it",
            name = "Temp",
            label = "tmp",
            value = Random.nextDouble(-10.0, 25.0),
            color = "#808080",
            classIcon = "wi wi-thermometer-exterior",
            isHidden = it % 2 != 0,
            measurementUnit = MeasurementUnit(
                abbreviation = "",
                id = "1",
                name = "tmp"
            )
        )
    }
    val favorites = (1..3).map {
        Favorite(
            createdAt = "",
            deviceId = "$it",
            id = "$it",
            updateAt = "",
            userId = "0"
        )
    }
    ImpulsMeteoTheme(
        darkTheme = true
    ) {
        ItemDevice(
            device = Device(
                id = "0",
                name = "Auto",
                key = "1223",
                status = false,
                updatedDate = Clock.System.now()
                    .toLocalDateTime(TimeZone.currentSystemDefault()),
                groups = listOf(
                    GroupDevice(
                        id = "1",
                        name = "Perm"
                    )
                ),
                params = params,
                host = "host",
                port = 8000,
                updateRate = 60000,
                videoUrl = "",
                notificationDevice = NotificationDevice(
                    deviceId = "1",
                    id = "0",
                    notificationParam = emptyList(),
                    userId = "8",
                    value = false
                )
            ),
            isEnableUpdate = true,
            favorites = favorites,
            configuration = LocalConfiguration.current,
            onClickDevice = {},
            onClickChangeFavorite = {}
        )
    }
}

/*
@Preview(
)
@Composable
private fun ItemDeviceView2() {
    val params = (1..7).map {
        Param(
            id = it,
            idUnit = 1,
            name = "Temp",
            label = "tmp",
            value = Random.nextDouble(-10.0, 25.0),
            color = "#808080",
            classIcon = "wi wi-thermometer-exterior",
            isHidden = it % 2 != 0,
            idDevice = 0
        )
    }
    ImpulsMeteoTheme {
        ItemDevice(
            device = DeviceOld(
                id = 0,
                name = "Auto",
                key = "1223",
                status = true,
                video = null,
                updatedAt = "12-03-2025",
                groups = listOf(
                    GroupDevicesOld(
                        id = 1,
                        name = "Perm"
                    )
                ),
                params = params,
                isFavorite = true,
                statusNotifications = true
            ),
            configuration = LocalConfiguration.current,
            onClickDevice = {},
            onDeleteClick = {},
            onClickChangeFavorite = {}
        )
    }
}*/

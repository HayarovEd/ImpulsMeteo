package com.edurda77.device_detail

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.edurda77.chart.SecondLineChart
import com.edurda77.domain.model.ElementHistory
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.model.Notifications
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.SingleDevice
import com.edurda77.domain.model.UnitMeteo
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.TEMPERATURE_ID
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDateContent
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiRowDeviceValueWithClick
import com.edurda77.resources.uikit.UiText
import com.edurda77.resources.uikit.asUiImageParam
import com.edurda77.resources.uikit.asUiText
import com.edurda77.resources.uikit.asUiTextParam

@SuppressLint("ConfigurationScreenWidthHeight")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    message: UiText?,
    dateFrom: String,
    dateTo: String,
    openFromDateDialog: () -> Unit,
    openToDateDialog: () -> Unit,
    isLoading: Boolean,
    device: SingleDevice?,
    currentLimit: Int,
    limits: List<Int>,
    isEnableEdit: Boolean,
    expandedLimits: Boolean,
    onClickChangeVisibleBottomSheet: () -> Unit,
    onClickExpandedUpdateDialog: () -> Unit,
    onClickChangeVisibleLimit: () -> Unit,
    onClickRequestHistory: (Int) -> Unit,
    onClickLimit: (Int) -> Unit,
    onAddNotificationToListClick: (Int, String, Int) -> Unit,
    onDeleteNotificationFromListClick: (Int) -> Unit,
    onUpdateNotificationInListClick: (Int, Int, Int, String, Int) -> Unit,
    onUpdateNotificationClick: () -> Unit,
    onChangeStatusClick: () -> Unit,
    onClickChangeFavorite: () -> Unit,
    onDeleteDevice: () -> Unit,
    onUpdateClick: (Param) -> Unit,
    sheetState: SheetState,
    showBottomSheet: Boolean,
    historyParams: List<Param>,
    isLoadingHistory: Boolean,
    histories: List<List<ElementHistory>>,
    screenWidth: Dp,
    units: List<UnitMeteo>,
) {
    val localDensity = LocalDensity.current
    val offsetXDropDownMenu = remember { mutableStateOf(0.dp) }
    val expandedDropDownloads = remember { mutableStateOf(false) }
    val configuration = LocalConfiguration.current

    UiBaseScaffold(
        message = message,
        topBarContent = {
            if (!isLoading) {
                Row(
                    modifier = modifier
                        .statusBarsPadding()
                        .fillMaxWidth()
                        .padding(horizontal = 15.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UiIconButton(
                        modifier = modifier,
                        icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        color = MaterialTheme.colorScheme.onBackground,
                        onClick = onBackClick
                    )
                    Column {
                        Text(
                            modifier = modifier,
                            text = device?.name ?: "",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.titleLarge,
                        )
                        Text(
                            modifier = modifier,
                            text = device?.key ?: "",
                            style = Typography.titleSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.outline,
                        )
                    }
                    Spacer(modifier = modifier.weight(1f))
                    if (isEnableEdit) {
                        UiIconButton(
                            modifier = modifier
                                .onGloballyPositioned { coordinates ->
                                    offsetXDropDownMenu.value =
                                        with(localDensity) { coordinates.positionInRoot().x.toDp() }
                                },
                            icon = ImageVector.vectorResource(R.drawable.three_dots),
                            color = MaterialTheme.colorScheme.onBackground,
                            onClick = {
                                expandedDropDownloads.value = true
                            }
                        )
                        DropdownMenu(
                            expanded = expandedDropDownloads.value,
                            containerColor = MaterialTheme.colorScheme.background,
                            offset = DpOffset(x = offsetXDropDownMenu.value * 0.6f, y = 0.dp),
                            onDismissRequest = {
                                expandedDropDownloads.value = false
                            }
                        ) {
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            R.drawable.bell
                                        ),
                                        contentDescription = ""
                                    )
                                },
                                onClick = onClickChangeVisibleBottomSheet,
                                text = {
                                    Text(
                                        modifier = modifier,
                                        text = stringResource(R.string.notifications),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = Typography.labelSmall,
                                    )
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (device?.isFavorite == true) ImageVector.vectorResource(
                                            R.drawable.baseline_star_24
                                        ) else ImageVector.vectorResource(
                                            R.drawable.baseline_star_border_24
                                        ),
                                        contentDescription = ""
                                    )
                                },
                                onClick = onClickChangeFavorite,
                                text = {
                                    Text(
                                        modifier = modifier,
                                        text = stringResource(R.string.favorite),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = Typography.labelSmall,
                                    )
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.pencil),
                                        contentDescription = ""
                                    )
                                },
                                onClick = onClickExpandedUpdateDialog,
                                text = {
                                    Text(
                                        modifier = modifier,
                                        text = stringResource(R.string.update_value),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = Typography.labelSmall,
                                    )
                                }
                            )
                            DropdownMenuItem(
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.trashcan),
                                        contentDescription = ""
                                    )
                                },
                                onClick = {
                                    expandedDropDownloads.value = false
                                    onDeleteDevice()
                                },
                                text = {
                                    Text(
                                        modifier = modifier,
                                        text = stringResource(R.string.delete_value),
                                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                                        style = Typography.labelSmall,
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },
        content = { innerPaddings ->
            if (showBottomSheet) {
                ModalBottomSheet(
                    modifier = modifier
                        .fillMaxWidth(),
                    onDismissRequest = onClickChangeVisibleBottomSheet,
                    sheetState = sheetState,
                    shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
                    containerColor = MaterialTheme.colorScheme.background
                ) {
                    NotificationsContent(
                        onClickChangeVisibleBottomSheet = onClickChangeVisibleBottomSheet,
                        onAddNotificationToListClick = { idParam, condition, value ->
                            onAddNotificationToListClick(
                                idParam,
                                condition,
                                value
                            )
                        },
                        onDeleteNotificationFromListClick = {
                            onDeleteNotificationFromListClick(it)
                        },
                        notifications = device?.notifications,
                        params = device?.params ?: emptyList(),
                        onUpdateNotificationInListClick = { index, id, idParam, condition, value ->
                            onUpdateNotificationInListClick(
                                index,
                                id,
                                idParam,
                                condition,
                                value
                            )
                        },
                        onChangeStatusClick = onChangeStatusClick,
                        onUpdateNotificationClick = onUpdateNotificationClick
                    )
                }
            }
            if (isLoading) {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = modifier.height(10.dp))
                    Text(
                        modifier = modifier
                            .fillMaxWidth(),
                        text = stringResource(R.string.loading),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = Typography.bodyLarge,
                        textAlign = TextAlign.Center,
                    )
                }
            } else {
                Column(
                    modifier = modifier
                        .padding(innerPaddings)
                        .navigationBarsPadding()
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 15.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                modifier = modifier,
                                text = "${stringResource(R.string.updated_data)} ${device?.updatedAt?:""}",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                            Text(
                                modifier = modifier,
                                text = "${stringResource(R.string.update)} ${(device?.frequency ?: 0) / 1000} ${
                                    stringResource(
                                        R.string.sec_unit
                                    )
                                }",
                                style = Typography.titleSmall,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                        Spacer(modifier = modifier.width(10.dp))
                        Box(
                            modifier = modifier
                                .clip(shape = RoundedCornerShape(100.dp))
                                .background(if (device?.status == true) MaterialTheme.colorScheme.outlineVariant else MaterialTheme.colorScheme.error)
                                .padding(10.dp),
                        ) {
                            Icon(
                                painter = if (device?.status == true) painterResource(R.drawable.checkmark) else painterResource(
                                    R.drawable.cross
                                ),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.background
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    LazyVerticalGrid(
                        modifier = modifier
                            .heightIn(max = 2000.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(15.dp),
                        verticalArrangement = Arrangement.spacedBy(15.dp),
                        columns = GridCells.Fixed(2)
                    ) {
                        items(historyParams) { param ->
                            val expandedDialog = remember { mutableStateOf(false) }
                            UiRowDeviceValueWithClick(
                                modifier = modifier.fillMaxWidth(0.4f),
                                image = if (param.idUnit == TEMPERATURE_ID && param.value >= 0.0) param.idUnit.asUiImageParam(
                                    true
                                ) else param.idUnit.asUiImageParam(),
                                value = param.value,
                                unit = param.idUnit.asUiTextParam(),
                                name = param.label,
                                hexColor = param.color,
                                content = {
                                    UpdateParamDialog(
                                        param = param,
                                        units = units,
                                        onCloseClick = {
                                            expandedDialog.value = false
                                        },
                                        onUpdateClick = {
                                            expandedDialog.value = false
                                            onUpdateClick(it)
                                        }
                                    )
                                },
                                expandedDialog = expandedDialog.value,
                                onCloseClick = {
                                    expandedDialog.value = false
                                },
                                onOpenClick = {
                                    expandedDialog.value = true
                                }
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    Card(
                        modifier = modifier
                            .width(screenWidth * 0.9f),
                        shape = RoundedCornerShape(10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.background
                        )
                    ) {
                        Column(
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(5.dp)
                        ) {
                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.filter),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.titleLarge,
                            )
                            Spacer(modifier = modifier.height(5.dp))
                            Row(
                                modifier = modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                UiDateContent(
                                    modifier = modifier.weight(1f),
                                    title = stringResource(R.string.date_from),
                                    content = dateFrom,
                                    onClick = openFromDateDialog
                                )
                                UiDateContent(
                                    modifier = modifier.weight(1f),
                                    title = stringResource(R.string.date_to),
                                    content = dateTo,
                                    onClick = openToDateDialog
                                )
                                UiDateContent(
                                    modifier = modifier
                                        .weight(1f)
                                        .onGloballyPositioned { coordinates ->
                                            offsetXDropDownMenu.value =
                                                with(localDensity) { coordinates.positionInRoot().x.toDp() }
                                        },
                                    title = stringResource(R.string.count_records),
                                    content = currentLimit.toString(),
                                    icon = null,
                                    onClick = onClickChangeVisibleLimit,
                                )
                                DropdownMenu(
                                    modifier = modifier,
                                    offset = DpOffset(x = offsetXDropDownMenu.value, y = 0.dp),
                                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                                    expanded = expandedLimits,
                                    onDismissRequest = onClickChangeVisibleLimit
                                ) {
                                    limits.forEachIndexed { index, limit ->
                                        DropdownMenuItem(
                                            text = {
                                                Text(
                                                    text = limit.toString(),
                                                    style = Typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                                )
                                            }, onClick = {
                                                onClickChangeVisibleLimit()
                                                onClickLimit(index)
                                            })
                                    }
                                }
                                IconButton(onClick = {
                                    onClickChangeVisibleLimit()
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                IconButton(onClick = {
                                    onClickRequestHistory(currentLimit)
                                }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(id = R.drawable.baseline_search_24),
                                        contentDescription = "",
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Spacer(modifier = modifier.height(35.dp))
                            if (isLoadingHistory) {
                                CircularProgressIndicator(
                                    modifier = modifier.align(Alignment.CenterHorizontally),
                                )
                            } else {
                            LazyColumn(
                                modifier = modifier
                                    .height(configuration.screenHeightDp.dp / 2)
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(35.dp)
                            ) {
                                itemsIndexed(histories) { index, history ->
                                        if (histories.isNotEmpty()) {
                                            Column(
                                                modifier = modifier
                                                    .fillMaxWidth()
                                            ) {
                                                val param = historyParams[index]
                                                Text(
                                                    modifier = modifier,
                                                    text = "${param.label}(${param.idUnit.asUiTextParam()})",
                                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                    style = Typography.titleLarge,
                                                )
                                                SecondLineChart(
                                                    modifier = modifier
                                                        .fillMaxWidth()
                                                        .aspectRatio(16 / 9f)
                                                        .padding(5.dp),
                                                    infos = history,
                                                    unit = param.idUnit.asUiTextParam(),
                                                    chartColor = MaterialTheme.colorScheme.outlineVariant,
                                                    textColor = MaterialTheme.colorScheme.onBackground,
                                                    maxValue = stringResource(R.string.max_value),
                                                    minValue = stringResource(R.string.min_value)
                                                )
                                            }
                                        } else {
                                            Text(
                                                modifier = modifier
                                                    .fillMaxWidth(),
                                                text = stringResource(R.string.not_data),
                                                color = MaterialTheme.colorScheme.onSurface,
                                                style = Typography.bodyLarge,
                                                textAlign = TextAlign.Center,
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showSystemUi = true
)
@Composable
private fun PortraitScreenView() {
    val params = remember {
        (0..5).map {
            Param(
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
    val units = remember {
        (0..5).map {
            UnitMeteo(
                id = it,
                name = "Param $it",
                short = "prm$it"
            )
        }
    }
    ImpulsMeteoTheme {
        PortraitScreen(
            onUpdateClick = {},
            onClickChangeFavorite = {},
            onClickRequestHistory = {},
            onClickLimit = {},
            onBackClick = {},
            message = DataError.DataStore.ERROR_READ_DATA.asUiText(),
            dateFrom = "01.04.2025",
            dateTo = "21.04.2025",
            isLoading = false,
            isEnableEdit = true,
            openFromDateDialog = {},
            openToDateDialog = {},
            device = SingleDevice(
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
                isFavorite = true,
                host = "host",
                port = 0,
                frequency = 60000,
                notifications = Notifications(
                    deviceStatus = true,
                    notifications = listOf(
                        NotificationDevice(
                            condition = "nt1",
                            idParam = 1,
                            value = 3
                        )
                    )
                )
            ),
            currentLimit = 1,
            limits = listOf(0, 1, 2, 3),
            expandedLimits = false,
            onClickChangeVisibleBottomSheet = {},
            onUpdateNotificationInListClick = { _, _, _, _, _ -> },
            onChangeStatusClick = {},
            onUpdateNotificationClick = {},
            onDeleteNotificationFromListClick = {},
            onClickChangeVisibleLimit = {},
            onClickExpandedUpdateDialog = {},
            onAddNotificationToListClick = { _, _, _ -> },
            screenWidth = 800.dp,
            sheetState = rememberModalBottomSheetState(),
            showBottomSheet = false,
            historyParams = params,
            isLoadingHistory = false,
            histories = emptyList(),
            units = units,
            onDeleteDevice = {}
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DirectoriesScreenView2() {
    val params = remember {
        (0..5).map {
            Param(
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
    val units = remember {
        (0..5).map {
            UnitMeteo(
                id = it,
                name = "Param $it",
                short = "prm$it"
            )
        }
    }
    ImpulsMeteoTheme {
        PortraitScreen(
            onUpdateClick = {},
            onClickChangeFavorite = {},
            onClickRequestHistory = {},
            onClickLimit = {},
            onBackClick = {},
            message = DataError.DataStore.ERROR_READ_DATA.asUiText(),
            dateFrom = "01.04.2025",
            dateTo = "21.04.2025",
            isLoading = false,
            isEnableEdit = true,
            openFromDateDialog = {},
            openToDateDialog = {},
            device = SingleDevice(
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
                isFavorite = true,
                host = "host",
                port = 0,
                frequency = 60000,
                notifications = Notifications(
                    deviceStatus = true,
                    notifications = listOf(
                        NotificationDevice(
                            condition = "nt1",
                            idParam = 1,
                            value = 3
                        )
                    )
                )
            ),
            currentLimit = 1,
            limits = listOf(0, 1, 2, 3),
            expandedLimits = false,
            onClickChangeVisibleBottomSheet = {},
            onUpdateNotificationInListClick = { _, _, _, _, _ -> },
            onChangeStatusClick = {},
            onUpdateNotificationClick = {},
            onDeleteNotificationFromListClick = {},
            onClickChangeVisibleLimit = {},
            onClickExpandedUpdateDialog = {},
            onAddNotificationToListClick = { _, _, _ -> },
            screenWidth = 800.dp,
            sheetState = rememberModalBottomSheetState(),
            showBottomSheet = false,
            historyParams = params,
            isLoadingHistory = false,
            histories = emptyList(),
            units = units,
            onDeleteDevice = {},
        )
    }
}
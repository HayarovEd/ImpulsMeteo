package com.edurda77.devices_list

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.domain.model.DeviceOld
import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.model.ParamOld
import com.edurda77.domain.utils.DEVICES_CREATE_LABEL
import com.edurda77.domain.utils.DEVICES_LIST_LABEL
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.ItemDevice
import com.edurda77.resources.uikit.NoAccess
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField
import com.edurda77.resources.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel
import kotlin.random.Random


@Composable
fun DevicesScreenRoot(
    onGoToLogin: () -> Unit,
    onGoToDevice: (String) -> Unit,
    viewModel: DevicesViewModel = koinViewModel(),
    configuration: Configuration,
    bottomBarContent: @Composable () -> Unit = {},
) {
    val context = LocalContext.current
    val version = context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: ""
    val state = viewModel.state.collectAsStateWithLifecycle()
    val snackBarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.eventFlow) { event ->
        when (event) {
            UiDevicesEvents.LoginNavigationEvent -> onGoToLogin()
            is UiDevicesEvents.OnError -> snackBarState.showSnackbar(event.message.asString(context))
        }
    }

    DevicesScreen(
        state = state.value,
        configuration = configuration,
        version = version,
        onGoToDevice = onGoToDevice,
        bottomBarContent = bottomBarContent,
        snackBarState = snackBarState,
        onEvent = viewModel::onEvent,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    modifier: Modifier = Modifier,
    state: DevicesState,
    configuration: Configuration,
    onGoToDevice: (String) -> Unit,
    bottomBarContent: @Composable () -> Unit = {},
    onEvent: (DevicesEvent) -> Unit,
    version: String,
    snackBarState: SnackbarHostState,
) {
    val windowSize = LocalWindowInfo.current.containerDpSize
    val listState = rememberLazyListState()
    val pagerState =
        rememberPagerState(
            pageCount = { state.filteredDevices.keys.size }
        )
    val scope = rememberCoroutineScope()
    val expandedAddDialog = remember { mutableStateOf(false) }


    LaunchedEffect(pagerState.currentPage) {
        if (state.filteredDevices.isNotEmpty()) {
            onEvent(DevicesEvent.SelectGroup(pagerState.currentPage))
            if (pagerState.currentPage > 0 && pagerState.currentPage != listState.layoutInfo.totalItemsCount - 1) {
                listState.animateScrollToItem(pagerState.currentPage - 1)
            }
        }
    }

    val isShowDialogLogOff = remember { mutableStateOf(false) }

    if (isShowDialogLogOff.value) {
        UiAlertDialog(
            title = stringResource(id = R.string.sure_exit),
            onClickConfirm = {
                isShowDialogLogOff.value = false
                onEvent(DevicesEvent.Logoff)
                onEvent(DevicesEvent.OnCloseWebSocket)
            },
            onClickCancel = {
                isShowDialogLogOff.value = false
            }
        )
    }

    if (expandedAddDialog.value) {
        UiDialog(
            onCloseDialog = {
                expandedAddDialog.value = false
            },
            content = {
                AddDeviceDialog(
                    groups = state.groups,
                    selectedGroups = state.selectedGroups,
                    onCloseClick = {
                        expandedAddDialog.value = false
                        onEvent(DevicesEvent.ClearSelectedGroups)
                    },
                    onAddClick = { name, key, frequency, groups ->
                        onEvent(
                            DevicesEvent.OnInsertDevice(
                                name = name,
                                key = key,
                                frequency = frequency,
                                groups = groups
                            )
                        )
                    },
                    onUpdateGroups = {
                        onEvent(DevicesEvent.UpdateSelectedGroups(it))
                    }
                )
            }
        )
    }
    UiBaseScaffold(
        message = null,
        snakeBarHostState = snackBarState,
        topBarContent = {
            Column(
                modifier = modifier
                    .statusBarsPadding()
                    .padding(horizontal = 15.dp)
                    .fillMaxWidth(),
            ) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    if (!state.isShowSearch) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            UiIconButton(
                                icon = ImageVector.vectorResource(
                                    id = R.drawable.baseline_search_24
                                ),
                                color = MaterialTheme.colorScheme.onBackground,
                                onClick = {
                                    onEvent(DevicesEvent.ShowSearchField)
                                    onEvent(DevicesEvent.OnSearch(""))
                                }
                            )
                            DevicesSelectorGroup(
                                modifier = modifier.weight(1f),
                                listState = listState,
                                devices = state.filteredDevices,
                                numberSelectedGroup = state.numberSelectedGroup,
                                pagerState = pagerState,
                                scope = scope,
                                screenWidth = windowSize.width,
                                onClick = {
                                    onEvent(DevicesEvent.SelectGroup(it))
                                }
                            )
                            if (state.enableUpdate) {
                                IconButton(
                                    enabled = !state.isUpdating,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    onClick = { onEvent(DevicesEvent.UpdateApp) }
                                ) {
                                    if (state.isUpdating) {
                                        Text(
                                            modifier = modifier,
                                            text = "${state.percentUpdate}%",
                                            style = Typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    } else {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.outline_update_24),
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            contentDescription = ""
                                        )
                                    }
                                }

                            }
                            UiIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.outline_sort_24),
                                color = MaterialTheme.colorScheme.onBackground,
                                onClick = { onEvent(DevicesEvent.SortDevicesByStatus) }
                            )
                            UiIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_logout_24),
                                color = MaterialTheme.colorScheme.onBackground,
                                onClick = { isShowDialogLogOff.value = true }
                            )
                        }
                    } else {
                        Row(
                            modifier = modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            UiIconButton(
                                icon = ImageVector.vectorResource(id = R.drawable.baseline_search_off_24),
                                color = MaterialTheme.colorScheme.onBackground,
                                onClick = {
                                    onEvent(DevicesEvent.ShowSearchField)
                                    onEvent(DevicesEvent.OnSearch(""))
                                }
                            )
                            UiTextField(
                                modifier = modifier.weight(1f),
                                content = state.query,
                                label = stringResource(id = R.string.search),
                                onClickContent = {
                                    onEvent(DevicesEvent.OnSearch(it))
                                })
                            if (state.enableUpdate) {
                                IconButton(
                                    enabled = !state.isUpdating,
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = MaterialTheme.colorScheme.primaryContainer
                                    ),
                                    onClick = { onEvent(DevicesEvent.UpdateApp) }
                                ) {
                                    if (state.isUpdating) {
                                        Text(
                                            modifier = modifier,
                                            text = "${state.percentUpdate}%",
                                            style = Typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    } else {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.outline_update_24),
                                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                            contentDescription = ""
                                        )
                                    }
                                }

                            }
                            UiIconButton(
                                color = MaterialTheme.colorScheme.onBackground,
                                icon = ImageVector.vectorResource(id = R.drawable.outline_sort_24),
                                onClick = { onEvent(DevicesEvent.SortDevicesByStatus) }
                            )
                            UiIconButton(
                                color = MaterialTheme.colorScheme.onBackground,
                                icon = ImageVector.vectorResource(id = R.drawable.logout),
                                onClick = { isShowDialogLogOff.value = true }
                            )
                        }
                        Spacer(modifier = modifier.height(10.dp))
                        DevicesSelectorGroup(
                            listState = listState,
                            devices = state.filteredDevices,
                            numberSelectedGroup = state.numberSelectedGroup,
                            onClick = {
                                onEvent(DevicesEvent.SelectGroup(it))
                            },
                            scope = scope,
                            pagerState = pagerState,
                            screenWidth = windowSize.width,
                        )
                    }
                } else {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        UiIconButton(
                            icon = if (state.isShowSearch) ImageVector.vectorResource(id = R.drawable.baseline_search_off_24) else ImageVector.vectorResource(
                                id = R.drawable.baseline_search_24
                            ),
                            color = MaterialTheme.colorScheme.onBackground,
                            onClick = {
                                onEvent(DevicesEvent.ShowSearchField)
                                onEvent(DevicesEvent.OnSearch(""))
                            }
                        )
                        if (!state.isShowSearch) {
                            Text(
                                modifier = modifier.weight(1f),
                                text = "${stringResource(id = R.string.version)}: $version",
                                style = Typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                textAlign = TextAlign.Center
                            )
                        } else UiTextField(
                            modifier = modifier.weight(1f),
                            content = state.query,
                            label = stringResource(id = R.string.search),
                            onClickContent = {
                                onEvent(DevicesEvent.OnSearch(it))
                            }
                        )
                        if (state.enableUpdate) {
                            IconButton(
                                enabled = !state.isUpdating,
                                colors = IconButtonDefaults.iconButtonColors(
                                    containerColor = MaterialTheme.colorScheme.primaryContainer
                                ),
                                onClick = { onEvent(DevicesEvent.UpdateApp) }
                            ) {
                                if (state.isUpdating) {
                                    Text(
                                        modifier = modifier,
                                        text = "${state.percentUpdate}%",
                                        style = Typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                } else {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.outline_update_24),
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        contentDescription = ""
                                    )
                                }
                            }

                        }
                        UiIconButton(
                            color = MaterialTheme.colorScheme.onBackground,
                            icon = ImageVector.vectorResource(id = R.drawable.outline_sort_24),
                            onClick = { onEvent(DevicesEvent.SortDevicesByStatus) }
                        )
                        UiIconButton(
                            color = MaterialTheme.colorScheme.onBackground,
                            icon = ImageVector.vectorResource(id = R.drawable.logout),
                            onClick = { isShowDialogLogOff.value = true }
                        )
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    DevicesSelectorGroup(
                        listState = listState,
                        devices = state.filteredDevices,
                        numberSelectedGroup = state.numberSelectedGroup,
                        onClick = {
                            onEvent(DevicesEvent.SelectGroup(it))
                        },
                        scope = scope,
                        pagerState = pagerState,
                        screenWidth = windowSize.width,
                    )
                }
            }
        },
        bottomBarContent = bottomBarContent,
        fabContent = {
            if (state.authUser?.permissions?.map { it.name }
                    ?.contains(DEVICES_CREATE_LABEL) == true) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.outlineVariant,
                    onClick = { expandedAddDialog.value = true }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.plus),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.background
                    )
                }
            }
        },
        content = { paddings ->
            PullToRefreshBox(
                modifier = modifier.padding(paddings),
                isRefreshing = state.isLoading,
                onRefresh = { onEvent(DevicesEvent.Refresh) },
                indicator = {
                    if (state.isLoading) {
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
                    }
                }
            ) {
                if (!state.isLoading) {
                    state.authUser?.let { user ->
                        if (user.permissions.map { it.name }.contains(DEVICES_LIST_LABEL)) {
                            HorizontalPager(
                                modifier = modifier
                                    .fillMaxSize()
                                    .padding(15.dp),
                                state = pagerState,
                                verticalAlignment = Alignment.Top
                            ) { page ->
                                val currentDevices = state.filteredDevices.values.toList()[page]
                                val cellsCount =
                                    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
                                LazyVerticalGrid(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    columns = GridCells.Fixed(cellsCount),
                                    verticalArrangement = Arrangement.spacedBy(15.dp),
                                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                                ) {
                                    items(
                                        items = currentDevices,
                                        key = {
                                            it.id
                                        }) { device ->
                                        ItemDevice(
                                            modifier = modifier,
                                            device = device,
                                            configuration = configuration,
                                            favorites = user.favorites,
                                            isEnableUpdate = device.id !in state.updatingDeviceIds,
                                            onClickDevice = {
                                                onGoToDevice(device.id)
                                            },
                                            onClickChangeFavorite = {
                                                onEvent(DevicesEvent.WorkWithFavorite(device))
                                            },
                                        )
                                    }
                                }
                            }
                        } else {
                            NoAccess()
                        }
                    }
                }
            }
        }
    )
}


@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DevicesScreenView1() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        DevicesScreen(
            state = DevicesState(),
            version = "1.0",
            snackBarState = snackBarState,
            configuration = LocalConfiguration.current,
            onGoToDevice = {},
            onEvent = {},
        )
    }
}

@Preview
@Composable
private fun DevicesScreenView2() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        DevicesScreen(
            state = DevicesState(),
            version = "1.0",
            snackBarState = snackBarState,
            configuration = LocalConfiguration.current,
            onGoToDevice = {},
            onEvent = {},
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DevicesScreenView3() {
    val snackBarState = remember { SnackbarHostState() }

    ImpulsMeteoTheme {
        DevicesScreen(
            state = DevicesState(
                isLoading = false,
                isShowSearch = true,
                updatingDeviceIds = emptyList()
            ),
            version = "1.0",
            snackBarState = snackBarState,
            configuration = LocalConfiguration.current,
            onGoToDevice = {},
            onEvent = {},
        )
    }
}

@Preview(
    showSystemUi = true,
)
@Composable
private fun DevicesScreenView4() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        DevicesScreen(
            state = DevicesState(
                isLoading = false,
                isShowSearch = true,
                updatingDeviceIds = emptyList()
            ),
            version = "1.0",
            snackBarState = snackBarState,
            configuration = LocalConfiguration.current,
            onGoToDevice = {},
            onEvent = {},
        )
    }
}

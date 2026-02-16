package com.edurda77.directories

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.domain.utils.DIRECTORY_EDIT
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField
import com.edurda77.resources.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel

@Composable
fun DirectoriesScreenRoot(
    onGoToLogin: () -> Unit,
    viewModel: DirectoriesViewModel = koinViewModel(),
    configuration: Configuration,
    bottomBarContent: @Composable () -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent

    val snackBarState = remember { SnackbarHostState() }
    val context = LocalContext.current


    ObserveAsEvents(viewModel.eventFlow) { event ->
        when (event) {
            UiDirectoriesEvents.LoginNavigationEvent -> onGoToLogin()
            is UiDirectoriesEvents.OnError -> snackBarState.showSnackbar(event.message.asString(context))
        }
    }

    DirectoriesScreen(
        state = state.value,
        configuration = configuration,
        bottomBarContent = bottomBarContent,
        snackBarState = snackBarState,
        onEvent = onEvent,
        onGoToLogin = onGoToLogin
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DirectoriesScreen(
    modifier: Modifier = Modifier,
    state: DirectoriesState,
    configuration: Configuration,
    snackBarState:SnackbarHostState,
    bottomBarContent: @Composable () -> Unit = {},
    onEvent: (DirectoriesEvent) -> Unit,
    onGoToLogin: () -> Unit,
) {


    val isShowDialogLogOff = remember { mutableStateOf(false) }
    val expandedAddDialog = remember { mutableStateOf(false) }


    if (isShowDialogLogOff.value) {
        UiAlertDialog(
            title = stringResource(id = R.string.sure_exit),
            onClickConfirm = {
                isShowDialogLogOff.value = false
                onEvent(DirectoriesEvent.Logoff)
                onGoToLogin()
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
                when (state.directoriesType) {
                    DirectoriesType.GROUPS -> {
                        AddDevicesGroupDialog(
                            onCloseClick = { expandedAddDialog.value = false },
                            onAddClick = { name ->
                                onEvent(DirectoriesEvent.AddDevicesGroup(name))
                            }
                        )
                    }

                    DirectoriesType.UNITS -> {
                        AddUnitDialog(
                            onCloseClick = { expandedAddDialog.value = false },
                            onAddClick = { name, short ->
                                onEvent(DirectoriesEvent.AddUnit(name = name, short = short))
                            }
                        )
                    }
                }
            }
        )
    }


    UiBaseScaffold(
        message = null,
        snakeBarHostState = snackBarState,
        topBarContent = {
            Row(
                modifier = modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                UiTextField(
                    modifier = modifier.weight(6f),
                    content = state.query,
                    label = stringResource(id = R.string.search),
                    onClickContent = {
                        onEvent(DirectoriesEvent.OnSearch(it))
                    })
                UiIconButton(
                    modifier = modifier.weight(1f),
                    color = MaterialTheme.colorScheme.onBackground,
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_logout_24),
                    onClick = { isShowDialogLogOff.value = true }
                )
            }
        },
        bottomBarContent = bottomBarContent,
        fabContent = {
            if (state.loggedUser?.permissions?.contains(DIRECTORY_EDIT) == true) {
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
            Column(
                modifier
                    .padding(paddings)
                    .padding(15.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    modifier = modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ItemTitleDirectory(
                        modifier = modifier.weight(1f),
                        title = stringResource(R.string.group_devices),
                        color = if (state.directoriesType == DirectoriesType.GROUPS) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                        backgroundColor = if (state.directoriesType == DirectoriesType.GROUPS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        onClick = {
                            onEvent(DirectoriesEvent.SwitchDirectoriesType(DirectoriesType.GROUPS))
                        }
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    ItemTitleDirectory(
                        modifier = modifier.weight(1f),
                        title = stringResource(R.string.units),
                        color = if (state.directoriesType == DirectoriesType.UNITS) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary,
                        backgroundColor = if (state.directoriesType == DirectoriesType.UNITS) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                        onClick = {
                            onEvent(DirectoriesEvent.SwitchDirectoriesType(DirectoriesType.UNITS))
                        }
                    )
                }
                Spacer(modifier = modifier.height(10.dp))
                PullToRefreshBox(
                    modifier = modifier,
                    isRefreshing = state.isLoading,
                    onRefresh = { onEvent(DirectoriesEvent.Refresh) },
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
                    if (state.loggedUser?.permissions?.contains(DIRECTORY_LIST) == true) {
                        val cellsCount =
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
                        when (state.directoriesType) {
                            DirectoriesType.GROUPS -> {
                                DirectoryScreenGroups(
                                    isLoading = state.isLoading,
                                    isEnableUpdate = state.loggedUser.permissions.contains(
                                        DIRECTORY_EDIT
                                    ),
                                    groups = state.groups,
                                    cellsCount = cellsCount,
                                    onDeleteClick = {
                                        onEvent(DirectoriesEvent.DeleteDevicesGroup(it))
                                    },
                                    titleDelete = stringResource(R.string.sure_delete_group),
                                    onUpdateClick = { id, name ->
                                        onEvent(
                                            DirectoriesEvent.UpdateDevicesGroup(
                                                id = id,
                                                name = name
                                            )
                                        )
                                    }
                                )
                            }

                            DirectoriesType.UNITS -> {
                                DirectoryScreenUnits(
                                    isLoading = state.isLoading,
                                    isEnableUpdate = state.loggedUser.permissions.contains(
                                        DIRECTORY_EDIT
                                    ),
                                    units = state.units,
                                    cellsCount = cellsCount,
                                    onDeleteClick = {
                                        onEvent(DirectoriesEvent.DeleteUnit(it))
                                    },
                                    titleDelete = stringResource(R.string.sure_delete_unit),
                                    onUpdateClick = { id, name, short ->
                                        onEvent(
                                            DirectoriesEvent.UpdateUnit(
                                                id = id,
                                                name = name,
                                                short = short
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    } else {
                        if (state.loggedUser != null) {
                            Box(
                                modifier = modifier
                                    .padding(paddings)
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    modifier = modifier
                                        .fillMaxWidth(),
                                    text = stringResource(R.string.no_access_this),
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
    )
}

@Preview(
    showSystemUi = true
)
@Composable
private fun DirectoriesScreenView() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        DirectoriesScreen(
            onGoToLogin = {},
            bottomBarContent = {},
            snackBarState = snackBarState,
            configuration = LocalConfiguration.current,
            state = DirectoriesState(),
            onEvent = {}
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DirectoriesScreenView2() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        DirectoriesScreen(
            onGoToLogin = {},
            bottomBarContent = {},
            configuration = LocalConfiguration.current,
            snackBarState = snackBarState,
            state = DirectoriesState(),
            onEvent = {}
        )
    }
}
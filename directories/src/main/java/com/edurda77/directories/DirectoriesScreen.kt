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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.domain.utils.DIRECTORY_EDIT
import com.edurda77.domain.utils.DIRECTORY_LIST
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DirectoriesScreen(
    modifier: Modifier = Modifier,
    onGoToLogin: () -> Unit,
    viewModel: DirectoriesViewModel = hiltViewModel(),
    configuration: Configuration,
    bottomBarContent: @Composable () -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
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
                when (state.value.directoriesType) {
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
        message = state.value.message,
        configuration = configuration,
        topBarContent = {
            Row(
                modifier = modifier
                    .padding(top = 50.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                UiIconButton(
                    modifier = modifier,
                    icon = ImageVector.vectorResource(id = R.drawable.baseline_logout_24),
                    onClick = { isShowDialogLogOff.value = true }
                )
            }
        },
        bottomBarContent = bottomBarContent,
        fabContent = {
            if (state.value.loggedUser?.permissions?.contains(DIRECTORY_EDIT) == true) {
                FloatingActionButton(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    onClick = { expandedAddDialog.value = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        },
        content = { paddings ->
            Column(
                modifier
                    .padding(paddings)
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
                        color = if (state.value.directoriesType == DirectoriesType.GROUPS) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
                        colorDivider = if (state.value.directoriesType == DirectoriesType.GROUPS) MaterialTheme.colorScheme.onPrimaryContainer else Color.Transparent,
                        onClick = {
                            onEvent(DirectoriesEvent.SwitchDirectoriesType(DirectoriesType.GROUPS))
                        }
                    )
                    ItemTitleDirectory(
                        modifier = modifier.weight(1f),
                        title = stringResource(R.string.units),
                        color = if (state.value.directoriesType == DirectoriesType.UNITS) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
                        colorDivider = if (state.value.directoriesType == DirectoriesType.UNITS) MaterialTheme.colorScheme.onPrimaryContainer else Color.Transparent,
                        onClick = {
                            onEvent(DirectoriesEvent.SwitchDirectoriesType(DirectoriesType.UNITS))
                        }
                    )
                }
                PullToRefreshBox(
                    modifier = modifier,
                    isRefreshing = state.value.isLoading,
                    onRefresh = { onEvent(DirectoriesEvent.Refresh) },
                    indicator = {
                        if (state.value.isLoading) {
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
                    if (state.value.loggedUser?.permissions?.contains(DIRECTORY_LIST) == true) {
                        val cellsCount =
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
                        when (state.value.directoriesType) {
                            DirectoriesType.GROUPS -> {
                                DirectoryScreenGroups(
                                    isLoading = state.value.isLoading,
                                    isEnableUpdate = state.value.loggedUser?.permissions?.contains(
                                        DIRECTORY_EDIT
                                    ) == true,
                                    groups = state.value.groups,
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
                                    isLoading = state.value.isLoading,
                                    isEnableUpdate = state.value.loggedUser?.permissions?.contains(
                                        DIRECTORY_EDIT
                                    ) == true,
                                    units = state.value.units,
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
                        if (state.value.loggedUser != null) {
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
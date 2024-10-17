package com.edurda77.users_list

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.domain.utils.USERS_CREATE
import com.edurda77.domain.utils.USERS_DELETE
import com.edurda77.domain.utils.USERS_EDIT
import com.edurda77.domain.utils.USERS_LIST
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersListScreen(
    modifier: Modifier = Modifier,
    onGoToLogin: () -> Unit,
    viewModel: UsersViewModel = hiltViewModel(),
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
                onEvent(UsersEvent.Logoff)
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
                onEvent(UsersEvent.ClearSelected)
            },
            content = {
                AddUserDialog(
                    devices = state.value.devices,
                    permissions = state.value.permissions,
                    selectedDevices = state.value.selectedDevices,
                    selectedPermissions = state.value.selectedPermissions,
                    onCloseClick = {
                        expandedAddDialog.value = false
                        onEvent(UsersEvent.ClearSelected)
                    },
                    onAddClick = { name, email, password, devices, permissions ->
                        onEvent(
                            UsersEvent.InsertNewUser(
                                name = name,
                                email = email,
                                password = password,
                                devices = devices,
                                permissions = permissions
                            )
                        )
                    },
                    onUpdatePermissions = {
                        onEvent(UsersEvent.UpdateSelectedPermission(it))
                    },
                    onUpdateDevices = {
                        onEvent(UsersEvent.UpdateSelectedDevice(it))
                    }
                )
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
            if (state.value.loggedUser?.permissions?.contains(USERS_CREATE) == true) {
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
            PullToRefreshBox(
                modifier = modifier.padding(paddings),
                isRefreshing = state.value.isLoading,
                onRefresh = { onEvent(UsersEvent.Refresh) },
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
                if (state.value.loggedUser?.permissions?.contains(USERS_LIST) == true) {
                    if (state.value.users.isNotEmpty() && !state.value.isLoading) {
                        val cellsCount =
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(15.dp),
                            columns = StaggeredGridCells.Fixed(cellsCount),
                            verticalItemSpacing = 5.dp,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            items(state.value.users) { user ->
                                ItemUser(
                                    user = user,
                                    onDeleteClick = {
                                        onEvent(UsersEvent.DeleteUser(it))
                                    },
                                    isEnabledDelete = state.value.loggedUser?.permissions?.contains(
                                        USERS_DELETE
                                    ) == true,
                                    isEnabledUpdate = state.value.loggedUser?.permissions?.contains(
                                        USERS_EDIT
                                    ) == true,
                                    onClearSelected = {
                                        onEvent(UsersEvent.ClearSelected)
                                    },
                                    onUpdateSelected = {
                                        onEvent(UsersEvent.UpdateSelected(user))
                                    },
                                    devices = state.value.devices,
                                    permissions = state.value.permissions,
                                    selectedDevices = state.value.selectedDevices,
                                    selectedPermissions = state.value.selectedPermissions,
                                    onUpdatePermissions = {
                                        onEvent(UsersEvent.UpdateSelectedPermission(it))
                                    },
                                    onUpdateDevices = {
                                        onEvent(UsersEvent.UpdateSelectedDevice(it))
                                    },
                                    onUpdateClick = { id, name, email, password, devices, permissions ->
                                        onEvent(
                                            UsersEvent.UpdateUser(
                                                id = id,
                                                name = name,
                                                email = email,
                                                password = password,
                                                devices = devices,
                                                permissions = permissions
                                            )
                                        )
                                    },
                                )
                            }
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
    )
}
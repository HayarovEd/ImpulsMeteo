package com.edurda77.users_list

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
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
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
import com.edurda77.domain.utils.USERS_CREATE_LABEL
import com.edurda77.domain.utils.USERS_DELETE_LABEL
import com.edurda77.domain.utils.USERS_EDIT_LABEL
import com.edurda77.domain.utils.USERS_LIST_LABEL
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.NoAccess
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField
import com.edurda77.resources.utils.ObserveAsEvents
import org.koin.androidx.compose.koinViewModel


@Composable
fun UsersListScreenRoot(
    onGoToLogin: () -> Unit,
    viewModel: UsersViewModel = koinViewModel(),
    configuration: Configuration,
    bottomBarContent: @Composable () -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackBarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.eventFlow) { event ->
        when (event) {
            UiUsersEvents.LoginNavigationEvent -> onGoToLogin()
            is UiUsersEvents.OnError -> snackBarState.showSnackbar(event.message.asString(context))
        }
    }

    val onEvent = viewModel::onEvent
    UsersListScreen(
        state = state.value,
        configuration = configuration,
        snackBarState = snackBarState,
        bottomBarContent = bottomBarContent,
        onGoToLogin = onGoToLogin,
        onEvent = onEvent
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun UsersListScreen(
    modifier: Modifier = Modifier,
    onGoToLogin: () -> Unit,
    configuration: Configuration,
    state: UsersState,
    snackBarState:SnackbarHostState,
    onEvent: (UsersEvent) -> Unit,
    bottomBarContent: @Composable () -> Unit = {},
) {

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
                    devices = state.devices,
                    permissions = state.permissions,
                    selectedDevices = state.selectedDevices,
                    selectedPermissions = state.selectedPermissions,
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
        message = null,
        snakeBarHostState = snackBarState,
        topBarContent = {
            Row(
                modifier = modifier
                    .statusBarsPadding()
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                UiTextField(
                    modifier = modifier.weight(6f),
                    content = state.query,
                    label = stringResource(id = R.string.search),
                    onClickContent = {
                        onEvent(UsersEvent.SearchUser(it))
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
            if (state.authUser?.permissions?.map { it.name }?.contains(USERS_CREATE_LABEL) == true) {
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
                onRefresh = { onEvent(UsersEvent.Refresh) },
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
                        val userPermissions = user.permissions.map { it.name }
                        if (userPermissions.contains(USERS_LIST_LABEL)) {
                            if (state.users.isNotEmpty()) {
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
                                    itemsIndexed(state.filteredUsers) { index, user ->
                                        ItemUser(
                                            user = user,
                                            onDeleteClick = {
                                                onEvent(UsersEvent.DeleteUser(it))
                                            },
                                            isEnabledDelete = userPermissions.contains(
                                                USERS_DELETE_LABEL
                                            ),
                                            isEnabledUpdate = userPermissions.contains(
                                                USERS_EDIT_LABEL
                                            ),
                                            onClearSelected = {
                                                onEvent(UsersEvent.ClearSelected)
                                            },
                                            onUpdateSelected = {
                                                // onEvent(UsersEvent.UpdateSelected(user))
                                            },
                                            devices = state.devices,
                                            permissions = state.permissions,
                                            selectedDevices = state.selectedDevices,
                                            selectedPermissions = state.selectedPermissions,
                                            isExpanded = user.isExpanded,
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
                                            onClickExpanded = {
                                                onEvent(UsersEvent.ExpandUser(index))
                                            }
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
showSystemUi = true
)
@Composable
private fun UsersListScreenView() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        UsersListScreen(
            onGoToLogin = {},
            bottomBarContent = {},
            configuration = LocalConfiguration.current,
            state = UsersState(),
            onEvent = {},
            snackBarState = snackBarState
        )
    }
}

@Preview(
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UsersListScreenView2() {
    val snackBarState = remember { SnackbarHostState() }
    ImpulsMeteoTheme {
        UsersListScreen(
            onGoToLogin = {},
            bottomBarContent = {},
            configuration = LocalConfiguration.current,
            state = UsersState(),
            onEvent = {},
            snackBarState = snackBarState
        )
    }
}
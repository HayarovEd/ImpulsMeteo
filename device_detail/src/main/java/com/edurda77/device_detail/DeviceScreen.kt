package com.edurda77.device_detail

import android.content.res.Configuration
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.domain.utils.DEVICES_EDIT_LABEL
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiAlertDialog
import com.edurda77.resources.uikit.UiDialog
import com.edurda77.resources.utils.ObserveAsEvents
import kotlinx.coroutines.flow.collectLatest
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceScreen(
    onBackClick: () -> Unit,
    viewModel: DeviceViewModel = koinViewModel(),
    configuration: Configuration,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val sheetState = rememberModalBottomSheetState()
    val expandedFromDateDialog = remember { mutableStateOf(false) }
    val expandedToDateDialog = remember { mutableStateOf(false) }
    val expandedLimits = remember { mutableStateOf(false) }
    val showBottomSheet = remember { mutableStateOf(false) }
    val expandedUpdateDialog = remember { mutableStateOf(false) }
    val isFilterOpen = remember { mutableStateOf(false) }
    val limits = listOf(100, 500, 1000, 1500)
    val currentLimit = remember { mutableIntStateOf(limits[0]) }
    val windowSize = LocalWindowInfo.current.containerDpSize
    val context = LocalContext.current


    val snackBarState = remember { SnackbarHostState() }

    ObserveAsEvents(viewModel.eventFlow) { event ->
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                UiDeviceEvents.LoginNavigationEvent -> onBackClick()
                is UiDeviceEvents.OnError -> snackBarState.showSnackbar(
                    event.message.asString(
                        context
                    )
                )
            }
        }
    }


    WheelDateTimePickerView(
        height = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) windowSize.height * 9 / 10 else windowSize.height / 5,
        startDate = state.value.fromDate,
        title = stringResource(R.string.choose_from_date),
        titleStyle = Typography.bodyLarge,
        doneLabelStyle = Typography.bodyLarge,
        doneLabel = stringResource(R.string.ok),
        dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
        showDatePicker = expandedFromDateDialog.value,
        onDoneClick = {
            onEvent(DeviceEvent.OnSetFromDate(it))
            expandedFromDateDialog.value = false
        },
        onDismiss = {
            expandedFromDateDialog.value = false
        }
    )

    WheelDateTimePickerView(
        height = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) windowSize.height * 9 / 10 else windowSize.height / 3,
        startDate = state.value.toDate,
        title = stringResource(R.string.choose_to_date),
        titleStyle = Typography.bodyLarge,
        doneLabelStyle = Typography.bodyLarge,
        doneLabel = stringResource(R.string.ok),
        dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
        showDatePicker = expandedToDateDialog.value,
        onDoneClick = {
            onEvent(DeviceEvent.OnSetToDate(it))
            expandedToDateDialog.value = false
        },
        onDismiss = {
            expandedToDateDialog.value = false
        }
    )

    val expandedDeleteDialog = remember { mutableStateOf(false) }
    if (expandedDeleteDialog.value) {
        UiAlertDialog(
            title = stringResource(R.string.sure_delete_device),
            onClickConfirm = {
                onEvent(DeviceEvent.DeleteDevice)
                expandedDeleteDialog.value = false
            },
            onClickCancel = {
                expandedDeleteDialog.value = false
            }
        )
    }


    if (expandedUpdateDialog.value) {
        UiDialog(
            onCloseDialog = {
                expandedUpdateDialog.value = false
                onEvent(DeviceEvent.BackStartGroups)
            },
            content = {
                state.value.device?.let { device ->
                    UpdateDeviceDialog(
                        onCloseClick = {
                            expandedUpdateDialog.value = false
                            onEvent(DeviceEvent.BackStartGroups)
                        },
                        label = device.name,
                        key = device.key,
                        frequency = device.updateRate,
                        groups = state.value.groups,
                        onUpdateClick = { currentName, currentKey, currentFrequency ->
                            onEvent(
                                DeviceEvent.UpdateDevice(
                                    name = currentName,
                                    key = currentKey,
                                    frequency = currentFrequency
                                )
                            )
                        },
                        onUpdateGroups = {
                            onEvent(DeviceEvent.UpdateSelectedGroups(it))
                        },
                        selectedGroups = device.groups
                    )
                }
            }
        )
    }
    state.value.device?.let { device->
        state.value.authUser?.let { user->
            val enabledEdit = user.permissions.map { it.name }.contains(DEVICES_EDIT_LABEL)
            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                LandscapeScreen(
                    snackBarState = snackBarState,
                    isLoading = state.value.isLoading,
                    device = device,
                    user = user,
                    onBackClick = onBackClick,
                    dateFrom = state.value.fromDate.date.toString(),
                    dateTo = state.value.toDate.date.toString(),
                    isOpenFilter = isFilterOpen.value,
                    currentLimit = currentLimit.intValue,
                    expandedLimits = expandedLimits.value,
                    sheetState = sheetState,
                    isEnableEdit = enabledEdit,
                    showBottomSheet = showBottomSheet.value,
                    limits = limits,
                    historyParams = device.params,
                    isLoadingHistory = state.value.isLoadingHistory,
                    histories = state.value.historyStates,
                    units = state.value.units,
                    openFilter = {
                        isFilterOpen.value = it
                    },
                    openFromDateDialog = {
                        expandedFromDateDialog.value = true
                    },
                    openToDateDialog = {
                        expandedToDateDialog.value = true
                    },
                    onClickLimit = {
                        currentLimit.intValue = limits[it]
                    },
                    onClickChangeVisibleLimit = {
                        expandedLimits.value = !expandedLimits.value
                    },
                    onClickRequestHistory = {
                        onEvent(DeviceEvent.GetHistory(it))
                    },
                    onClickChangeVisibleBottomSheet = {
                        showBottomSheet.value = !showBottomSheet.value
                    },
                    onAddNotificationToListClick = { idParam, condition, value ->
                        onEvent(
                            DeviceEvent.AddNewNotificationToList(
                                idParam = idParam,
                                condition = condition,
                                value = value
                            )
                        )
                    },
                    onDeleteNotificationFromListClick = {
                        onEvent(
                            DeviceEvent.DeleteNotificationFromList(it)
                        )
                    },
                    onUpdateNotificationInListClick = { index, id, idParam, condition, value ->
                        onEvent(
                            DeviceEvent.UpdateNotificationInList(
                                index = index,
                                id = id,
                                idParam = idParam,
                                condition = condition,
                                value = value
                            )
                        )
                    },
                    onChangeStatusClick = {
                        onEvent(DeviceEvent.ChangeStatusNotifications)
                    },
                    onUpdateNotificationClick = {
                        onEvent(DeviceEvent.UpdateNotifications)
                        showBottomSheet.value = !showBottomSheet.value
                    },
                    onClickExpandedUpdateDialog = {
                        expandedUpdateDialog.value = true
                    },
                    onUpdateClick = { param ->
                        onEvent(DeviceEvent.UpdateParam(param))
                    },
                    onClickChangeFavorite = {
                        onEvent(DeviceEvent.WorkWithFavorite)
                    },
                    onDeleteDevice = {
                        expandedDeleteDialog.value = true
                    },
                    onClickClearSensors = {
                        onEvent(DeviceEvent.ClearDeviceSensorData)
                    }
                )
            } else {
                PortraitScreen(
                    snackBarState = snackBarState,
                    isLoading = state.value.isLoading,
                    device = device,
                    user = user,
                    onBackClick = onBackClick,
                    dateFrom = state.value.fromDate.date.toString(),
                    dateTo = state.value.toDate.date.toString(),
                    currentLimit = currentLimit.intValue,
                    expandedLimits = expandedLimits.value,
                    sheetState = sheetState,
                    isEnableEdit = enabledEdit,
                    showBottomSheet = showBottomSheet.value,
                    limits = limits,
                    historyParams = state.value.device?.params ?: emptyList(),
                    screenWidth = windowSize.width,
                    isLoadingHistory = state.value.isLoadingHistory,
                    histories = state.value.historyStates,
                    units = state.value.units,
                    openFromDateDialog = {
                        expandedFromDateDialog.value = true
                    },
                    openToDateDialog = {
                        expandedToDateDialog.value = true
                    },
                    onClickLimit = {
                        currentLimit.intValue = limits[it]
                    },
                    onClickChangeVisibleLimit = {
                        expandedLimits.value = !expandedLimits.value
                    },
                    onClickRequestHistory = {
                        onEvent(DeviceEvent.GetHistory(it))
                    },
                    onClickChangeVisibleBottomSheet = {
                        showBottomSheet.value = !showBottomSheet.value
                    },
                    onAddNotificationToListClick = { idParam, condition, value ->
                        onEvent(
                            DeviceEvent.AddNewNotificationToList(
                                idParam = idParam,
                                condition = condition,
                                value = value
                            )
                        )
                    },
                    onDeleteNotificationFromListClick = {
                        onEvent(
                            DeviceEvent.DeleteNotificationFromList(it)
                        )
                    },
                    onUpdateNotificationInListClick = { index, id, idParam, condition, value ->
                        onEvent(
                            DeviceEvent.UpdateNotificationInList(
                                index = index,
                                id = id,
                                idParam = idParam,
                                condition = condition,
                                value = value
                            )
                        )
                    },
                    onChangeStatusClick = {
                        onEvent(DeviceEvent.ChangeStatusNotifications)
                    },
                    onUpdateNotificationClick = {
                        onEvent(DeviceEvent.UpdateNotifications)
                        showBottomSheet.value = !showBottomSheet.value
                    },
                    onClickExpandedUpdateDialog = {
                        expandedUpdateDialog.value = true
                    },
                    onUpdateClick = { param ->
                        onEvent(DeviceEvent.UpdateParam(param))
                    },
                    onClickChangeFavorite = {
                        onEvent(DeviceEvent.WorkWithFavorite)
                    },
                    onDeleteDevice = {
                        expandedDeleteDialog.value = true
                    },
                    onClickClearSensors = {
                        onEvent(DeviceEvent.ClearDeviceSensorData)
                    },
                )
            }
        }
    }

}

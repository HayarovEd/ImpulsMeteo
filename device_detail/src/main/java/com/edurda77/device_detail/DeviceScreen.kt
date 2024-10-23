package com.edurda77.device_detail

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView

@Composable
fun DeviceScreen(
    onBackClick: () -> Unit,
    viewModel: DeviceViewModel = hiltViewModel(),
    configuration: Configuration,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val expandedFromDateDialog = remember { mutableStateOf(false) }
    val expandedToDateDialog = remember { mutableStateOf(false) }
    val isFilterOpen = remember { mutableStateOf(false) }
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp


    WheelDateTimePickerView(
        height = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) screenHeight * 9 / 10 else screenHeight / 5,
        startDate = state.value.fromDate,
        title = stringResource(R.string.choose_from_date),
        titleStyle = Typography.bodyLarge,
        doneLabelStyle = Typography.bodyLarge,
        doneLabel = stringResource(R.string.ok),
        dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
        showDatePicker = expandedFromDateDialog.value,
        onDoneClick = {
            onEvent(DeviceEvent.onSetFromDate(it))
        },
        onDismiss = {
            expandedFromDateDialog.value = false
        }
    )

    WheelDateTimePickerView(
        height = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) screenHeight * 9 / 10 else screenHeight / 3,
        startDate = state.value.toDate,
        title = stringResource(R.string.choose_to_date),
        titleStyle = Typography.bodyLarge,
        doneLabelStyle = Typography.bodyLarge,
        doneLabel = stringResource(R.string.ok),
        dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
        showDatePicker = expandedToDateDialog.value,
        onDoneClick = {
            onEvent(DeviceEvent.onSetToDate(it))
        },
        onDismiss = {
            expandedToDateDialog.value = false
        }
    )


    if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

    } else {
        PortraitScreen(
            configuration = configuration,
            message = state.value.message,
            isLoading = state.value.isLoading,
            device = state.value.device,
            onBackClick = onBackClick,
            dateFrom = state.value.fromDate.date.toString(),
            dateTo = state.value.toDate.date.toString(),
            isOpenFilter = isFilterOpen.value,
            openFilter = {
                isFilterOpen.value = it
            },
            openFromDateDialog = {
                expandedFromDateDialog.value = true
            },
            openToDateDialog = {
                expandedToDateDialog.value = true
            }
        )
    }
}

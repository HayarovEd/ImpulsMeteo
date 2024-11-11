package com.edurda77.device_detail

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.HistoryState
import com.edurda77.domain.model.NavigationRoute
import com.edurda77.domain.model.NotificationDevice
import com.edurda77.domain.usecase.AddFavoriteUseCase
import com.edurda77.domain.usecase.CloseWebsocketUseCase
import com.edurda77.domain.usecase.DeviceByIdUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.HistoryUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.RemoveFavoriteUseCase
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateDeviceUseCase
import com.edurda77.domain.usecase.UpdateNotificationsDeviceUseCase
import com.edurda77.domain.usecase.UpdateParamUseCase
import com.edurda77.domain.usecase.WebSocketUseCase
import com.edurda77.domain.utils.NEGATIVE_ID
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.TAKED_COUNT
import com.edurda77.domain.utils.convertToStringDateTime
import com.edurda77.domain.utils.updateDevice
import com.edurda77.resources.uikit.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeviceViewModel @Inject constructor(
    private val deviceByIdUseCase: DeviceByIdUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val updateNotificationsDeviceUseCase: UpdateNotificationsDeviceUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val updateDeviceUseCase: UpdateDeviceUseCase,
    private val unitsUseCase: UnitsUseCase,
    private val updateParamUseCase: UpdateParamUseCase,
    private val addFavoriteUseCase: AddFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val closeWebsocketUseCase: CloseWebsocketUseCase,
    private val historyUseCase: HistoryUseCase,
) : ViewModel() {
    private var _state = MutableStateFlow(DeviceState())
    val state = _state.asStateFlow()
    private var _startGroups = MutableStateFlow<List<GroupDevices>>(emptyList())

    init {
        loadLocalData()
    }

    fun onEvent(event: DeviceEvent) {
        when (event) {
            is DeviceEvent.OnSetFromDate -> {
                _state.value.copy(
                    fromDate = event.dateTime
                )
                    .updateState()
            }

            is DeviceEvent.OnSetToDate -> {
                _state.value.copy(
                    toDate = event.dateTime
                )
                    .updateState()
            }

            is DeviceEvent.GetHistory -> {
                loadHistory(event.limit)
            }

            is DeviceEvent.AddNewNotificationToList -> {
                viewModelScope.launch {
                    val updateList =
                        state.value.device?.notifications?.notifications?.toMutableList()
                    updateList?.add(
                        NotificationDevice(
                            condition = event.condition,
                            idParam = event.idParam,
                            value = event.value
                        )
                    )
                    if (state.value.device != null) {
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notifications = state.value.device!!.notifications.copy(
                                    notifications = updateList ?: emptyList()
                                )
                            )
                        )
                            .updateState()
                    }
                }
            }

            is DeviceEvent.DeleteNotificationFromList -> {
                viewModelScope.launch {
                    val updateList =
                        state.value.device?.notifications?.notifications?.toMutableList()
                    updateList?.removeAt(event.index)
                    if (state.value.device != null) {
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notifications = state.value.device!!.notifications.copy(
                                    notifications = updateList ?: emptyList()
                                )
                            )
                        )
                            .updateState()
                    }
                }
            }

            is DeviceEvent.UpdateNotificationInList -> {
                viewModelScope.launch {
                    val updateList =
                        state.value.device?.notifications?.notifications?.toMutableList()
                    if (state.value.device != null) {
                        updateList?.set(
                            event.index, NotificationDevice(
                                id = event.id,
                                condition = event.condition,
                                idParam = event.idParam,
                                value = event.value
                            )
                        )
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notifications = state.value.device!!.notifications.copy(
                                    notifications = updateList ?: emptyList()
                                )
                            )
                        )
                            .updateState()
                    }
                }
            }

            DeviceEvent.ChangeStatusNotifications -> {
                viewModelScope.launch {
                    if (state.value.device != null) {
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notifications = state.value.device!!.notifications.copy(
                                    deviceStatus = !state.value.device!!.notifications.deviceStatus
                                )
                            )
                        )
                            .updateState()
                    }
                }
            }

            DeviceEvent.UpdateNotifications -> {
                if (state.value.device != null) {
                    viewModelScope.launch {
                        when (val result = updateNotificationsDeviceUseCase.invoke(
                            token = state.value.token,
                            id = state.value.deviceId,
                            notifications = state.value.device!!.notifications
                        )) {
                            is ResultWork.Error -> {
                                _state.value.copy(
                                    message = result.error.asUiText()
                                )
                                    .updateState()
                            }

                            is ResultWork.Success -> {
                                loadDevice()
                            }
                        }
                    }
                }
            }

            is DeviceEvent.UpdateSelectedGroups -> {
                if (state.value.device != null) {
                    val updatedGroups = state.value.device!!.groups.toMutableList()
                    if (updatedGroups.contains(event.groupDevices)) {
                        updatedGroups.remove(event.groupDevices)
                    } else {
                        updatedGroups.add(event.groupDevices)
                    }
                    _state.value.copy(
                        device = state.value.device!!.copy(
                            groups = updatedGroups
                        )
                    )
                        .updateState()
                }
            }

            is DeviceEvent.UpdateDevice -> {
                if (state.value.device != null) {
                    viewModelScope.launch {
                        val device = state.value.device!!.copy(
                            name = event.name,
                            frequency = event.frequency.toIntOrNull() ?: 0,
                            key = event.key
                        )
                        when (val result = updateDeviceUseCase.invoke(
                            token = state.value.token,
                            device = device
                        )) {
                            is ResultWork.Error -> {
                                _state.value.copy(
                                    message = result.error.asUiText(),
                                    device = state.value.device!!.copy(
                                        groups = _startGroups.value
                                    )
                                )
                                    .updateState()
                            }

                            is ResultWork.Success -> {
                                loadDevice()
                            }
                        }
                    }
                }
            }

            DeviceEvent.BackStartGroups -> {
                if (state.value.device != null) {
                    _state.value.copy(
                        device = state.value.device!!.copy(
                            groups = _startGroups.value
                        )
                    )
                        .updateState()
                }
            }

            is DeviceEvent.UpdateParam -> {
                if (state.value.device != null) {
                    viewModelScope.launch {
                        when (val result = updateParamUseCase.invoke(
                            token = state.value.token,
                            param = event.param
                        )) {
                            is ResultWork.Error -> {
                                _state.value.copy(
                                    message = result.error.asUiText()
                                )
                                    .updateState()
                            }

                            is ResultWork.Success -> {
                                loadDevice()
                            }
                        }
                    }
                }
            }

            is DeviceEvent.WorkWithFavorite -> {
                viewModelScope.launch {
                    if (state.value.device?.isFavorite == true) {
                        removeFavoriteUseCase.invoke(
                            deviceId = state.value.deviceId,
                        )
                    } else {
                        addFavoriteUseCase.invoke(
                            deviceId = state.value.deviceId,
                        )
                    }
                }
            }
        }
    }


    private fun loadLocalData() {
        viewModelScope.launch {
            localTokenUseCase.invoke().collect { collectedToken ->
                when (collectedToken) {
                    is ResultWork.Error -> {
                        _state.value.copy(
                            isLoading = false,
                            message = collectedToken.error.asUiText()
                        )
                            .updateState()
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            token = collectedToken.data.accessToken
                        )
                            .updateState()
                        delay(500)
                        loadLoggedUserData(collectedToken.data.accessToken)
                    }
                }
            }
        }
    }

    private suspend fun loadLoggedUserData(token: String) {
        val deviceId = savedStateHandle.toRoute<NavigationRoute.Device>().id

        _state.value.copy(
            deviceId = deviceId.toIntOrNull() ?: NEGATIVE_ID
        )
            .updateState()
        delay(500)
        when (val result = loggedUserUseCase.invoke(token)) {
            is ResultWork.Error -> {
                _state.value.copy(
                    isLoading = false,
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    loggedUser = result.data
                )
                    .updateState()
                viewModelScope.launch {
                    loadDevice()
                }
                viewModelScope.launch {
                    loadGroups()
                    loadUnits()
                }
                viewModelScope.launch {
                    loadUpdateData()
                }
                viewModelScope.launch {
                    loadHistory(limit = 100)
                }
            }
        }
    }

    private suspend fun loadDevice() {
        val deviceId = state.value.deviceId
        if (deviceId != NEGATIVE_ID) {
            deviceByIdUseCase.invoke(
                token = state.value.token,
                id = deviceId
            ).collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {
                        _state.value.copy(
                            isLoading = false,
                            message = collector.error.asUiText()
                        )
                            .updateState()
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            isLoading = false,
                            device = collector.data
                        )
                            .updateState()
                        _startGroups.value = collector.data.groups
                    }
                }
            }
        }
    }

    private suspend fun loadGroups() {
        when (val result = devicesGroupsUseCase.invoke(state.value.token)) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    groups = result.data
                )
                    .updateState()
            }
        }
    }

    private suspend fun loadUnits() {
        when (val result = unitsUseCase.invoke(state.value.token)) {
            is ResultWork.Error -> {
                _state.value.copy(
                    message = result.error.asUiText()
                )
                    .updateState()
            }

            is ResultWork.Success -> {
                _state.value.copy(
                    units = result.data
                )
                    .updateState()
            }
        }
    }

    private suspend fun loadUpdateData() {

        webSocketUseCase.invoke(
            token = state.value.token,
            ids = listOf(state.value.deviceId)
        ).collect { collector ->
            when (collector) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        message = collector.error.asUiText()
                    )
                        .updateState()
                }

                is ResultWork.Success -> {
                    if (state.value.device != null && state.value.device!!.id == collector.data.id) {
                        _state.value.copy(
                            device = updateDevice(
                                device = state.value.device!!,
                                newDevice = collector.data
                            )
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    private fun loadHistory(limit: Int) {
        _state.value.copy(
            isLoadingHistory = true,
            historyStates = List(TAKED_COUNT) { HistoryState.Empty },
        )
            .updateState()
        viewModelScope.launch {
            when (val result = historyUseCase.invoke(
                token = state.value.token,
                id = state.value.deviceId,
                fromDate = convertToStringDateTime(state.value.fromDate),
                toDate = convertToStringDateTime(state.value.toDate),
                limit = limit
            )) {
                is ResultWork.Error -> {
                    Log.d("TEST HISTORY DEVICE", "error ${result.error}")
                    _state.value.copy(
                        isLoadingHistory = false,
                        message = result.error.asUiText()
                    )
                        .updateState()
                }

                is ResultWork.Success -> {
                    result.data.forEach {
                        if (it is HistoryState.Success)
                            Log.d("TEST HISTORY DEVICE", "success ${it.history}")
                    }
                    _state.value.copy(
                        isLoadingHistory = false,
                        historyStates = result.data
                    )
                        .updateState()
                }
            }
        }
    }


    private fun DeviceState.updateState() {
        _state.update {
            this
        }
    }

    /* override fun onCleared() {
         super.onCleared()
         viewModelScope.launch {
             closeWebsocketUseCase.invoke()
         }
     }*/
}
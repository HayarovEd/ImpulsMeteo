package com.edurda77.device_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edurda77.domain.model.GroupDevicesOld
import com.edurda77.domain.usecase.DeleteDeviceUseCase
import com.edurda77.domain.usecase.DeleteParamUseCase
import com.edurda77.domain.usecase.DeviceByIdUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.HistoryUseCase
import com.edurda77.domain.usecase.LocalTokenUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.RemoveFavoriteUseCase
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateDeviceUseCase
import com.edurda77.domain.usecase.UpdateFavoriteUseCase
import com.edurda77.domain.usecase.UpdateNotificationsDeviceUseCase
import com.edurda77.domain.usecase.UpdateParamUseCase
import com.edurda77.domain.usecase.WebSocketUseCaseOld
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.domain.utils.convertToStringDateTime
import com.edurda77.domain.utils.convertToStringDateTimeForHistory
import com.edurda77.resources.model.NavigationRoute
import com.edurda77.resources.uikit.asUiText
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

class DeviceViewModel(
    private val deviceByIdUseCase: DeviceByIdUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    private val localTokenUseCase: LocalTokenUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val updateNotificationsDeviceUseCase: UpdateNotificationsDeviceUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val updateDeviceUseCase: UpdateDeviceUseCase,
    private val unitsUseCase: UnitsUseCase,
    private val updateParamUseCase: UpdateParamUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val removeFavoriteUseCase: RemoveFavoriteUseCase,
    private val webSocketUseCaseOld: WebSocketUseCaseOld,
    private val historyUseCase: HistoryUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
    private val deleteParamUseCase: DeleteParamUseCase,
) : ViewModel() {
    private val deviceId = savedStateHandle.toRoute<NavigationRoute.Device>().id
    private var _state = MutableStateFlow(DeviceState())
    val state = _state
        .onStart {
            loadUserData()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DeviceState()
        )
    private var _startGroups = MutableStateFlow<List<GroupDevicesOld>>(emptyList())


    private val _eventFlow = Channel<UiDeviceEvents>()
    val eventFlow = _eventFlow.receiveAsFlow()

    private val mutex = Mutex()

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
                /*viewModelScope.launch {
                    val updateList =
                        state.value.device?.notificationsOld?.notifications?.toMutableList()
                    updateList?.add(
                        NotificationDeviceOld(
                            condition = event.condition,
                            idParam = event.idParam,
                            value = event.value
                        )
                    )
                    if (state.value.device != null) {
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notificationsOld = state.value.device!!.notificationsOld.copy(
                                    notifications = updateList ?: emptyList()
                                )
                            )
                        )
                            .updateState()
                    }
                }*/
            }

            is DeviceEvent.DeleteNotificationFromList -> {
                /*viewModelScope.launch {
                    val updateList =
                        state.value.device?.notificationsOld?.notifications?.toMutableList()
                    updateList?.removeAt(event.index)
                    if (state.value.device != null) {
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notificationsOld = state.value.device!!.notificationsOld.copy(
                                    notifications = updateList ?: emptyList()
                                )
                            )
                        )
                            .updateState()
                    }
                }*/
            }

            is DeviceEvent.UpdateNotificationInList -> {
                /*viewModelScope.launch {
                    val updateList =
                        state.value.device?.notificationsOld?.notifications?.toMutableList()
                    if (state.value.device != null) {
                        updateList?.set(
                            event.index, NotificationDeviceOld(
                                id = event.id,
                                condition = event.condition,
                                idParam = event.idParam,
                                value = event.value
                            )
                        )
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notificationsOld = state.value.device!!.notificationsOld.copy(
                                    notifications = updateList ?: emptyList()
                                )
                            )
                        )
                            .updateState()
                    }
                }*/
            }

            DeviceEvent.ChangeStatusNotifications -> {
                /*viewModelScope.launch {
                    if (state.value.device != null) {
                        _state.value.copy(
                            device = state.value.device?.copy(
                                notificationsOld = state.value.device!!.notificationsOld.copy(
                                    deviceStatus = !state.value.device!!.notificationsOld.deviceStatus
                                )
                            )
                        )
                            .updateState()
                    }
                }*/
            }

            DeviceEvent.UpdateNotifications -> {
                /*if (state.value.device != null) {
                    viewModelScope.launch {
                        when (val result = updateNotificationsDeviceUseCase.invoke(
                            token = state.value.token,
                            id = state.value.deviceId,
                            notificationsOld = state.value.device!!.notificationsOld
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
                }*/
            }

            is DeviceEvent.UpdateSelectedGroups -> {
                /* if (state.value.device != null) {
                     val updatedGroups = state.value.device!!.groups.toMutableList()
                     if (updatedGroups.contains(event.groupDevicesOld)) {
                         updatedGroups.remove(event.groupDevicesOld)
                     } else {
                         updatedGroups.add(event.groupDevicesOld)
                     }
                     _state.value.copy(
                         device = state.value.device!!.copy(
                             groups = updatedGroups
                         )
                     )
                         .updateState()
                 }*/
            }

            is DeviceEvent.UpdateDevice -> {
                /*if (state.value.device != null) {
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
                }*/
            }

            DeviceEvent.BackStartGroups -> {
                /* if (state.value.device != null) {
                     _state.value.copy(
                         device = state.value.device!!.copy(
                             groups = _startGroups.value
                         )
                     )
                         .updateState()
                 }*/
            }

            is DeviceEvent.UpdateParam -> {
                /*if (state.value.device != null) {
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
                }*/
            }

            is DeviceEvent.WorkWithFavorite -> {
                /*viewModelScope.launch {
                    state.value.device?.let { device ->
                        if (device.isFavorite) {
                            when (val result = removeFavoriteUseCase.invoke(
                                deviceId = device.id,
                                token = state.value.token
                            )) {
                                is ResultWork.Error -> {
                                    _state.value.copy(
                                        message = result.error.asUiText(),
                                    )
                                        .updateState()
                                }

                                is ResultWork.Success -> {
                                    _state.value.copy(
                                        device = device.copy(isFavorite = false),
                                    )
                                        .updateState()
                                }
                            }
                        } else {
                           *//* when (val result = updateFavoriteUseCase.invoke(
                                deviceId = device.id,
                                token = state.value.token
                            )) {
                                is ResultWork.Error -> {
                                    _state.value.copy(
                                        message = result.error.asUiText(),
                                    )
                                        .updateState()
                                }

                                is ResultWork.Success -> {
                                    _state.value.copy(
                                        device = device.copy(isFavorite = true),
                                    )
                                        .updateState()
                                }
                            }*//*
                        }
                    }
                }*/
            }

            DeviceEvent.DeleteDevice -> {
                /*viewModelScope.launch {
                    _state.value.copy(
                        isLoading = true,
                    )
                        .updateState()
                    when (val result = deleteDeviceUseCase.invoke(
                        token = state.value.token,
                        isFavorite = state.value.device?.isFavorite ?: false,
                        id = state.value.deviceId
                    )) {
                        is ResultWork.Error -> {
                            _state.value.copy(
                                isLoading = false,
                                message = result.error.asUiText()
                            )
                                .updateState()
                        }

                        is ResultWork.Success -> {
                            _state.value.copy(
                                isLoading = false,
                            )
                                .updateState()
                            _eventFlow.emit(UiDeviceEvents.BackNavigationEvent)
                        }
                    }
                }*/
            }

            DeviceEvent.ClearDeviceSensorData -> {
                clearSensors()
            }
        }
    }

    private fun loadUserData() {
        _state.value.copy(
            isLoading = true,
        )
            .updateState()
        viewModelScope.launch {
            when (val result = loggedUserUseCase.invoke()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        authUser = result.data,
                    )
                        .updateState()
                    loadDevice()
                    loadGroupsAndUnits()
                    loadHistory(limit = 100)
                }
            }
        }
    }


    private suspend fun loadLoggedUserData() {

        /*when (val result = loggedUserUseCase.invoke(token)) {
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
                    loadUpdateData()
                }
                viewModelScope.launch {

                }
            }
        }*/
    }

    private fun loadDevice() {
        viewModelScope.launch {
            when (val result = deviceByIdUseCase.invoke(deviceId)) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        device = result.data,
                        isLoading = false
                    )
                        .updateState()
                }
            }
        }
    }

    private fun loadGroupsAndUnits () {
        viewModelScope.launch {
            val resultGroupsDiff = async{ devicesGroupsUseCase.invoke() }
            val resultUnitsDiff = async { unitsUseCase.invoke() }
            when (val result = resultGroupsDiff.await()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        groups = result.data
                    )
                        .updateState()
                }
            }
            when (val result = resultUnitsDiff.await()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        units = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private suspend fun loadUpdateData() {

      /*  webSocketUseCaseOld.invoke(
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
                                newDeviceOld = collector.data
                            )
                        )
                            .updateState()
                    }
                }
            }
        }*/
    }

    private fun loadHistory(limit: Int) {
        _state.value.copy(
            isLoadingHistory = true,
            historyStates = emptyList(),
        )
            .updateState()
        viewModelScope.launch {
            when (val result = historyUseCase.invoke(
                id = deviceId,
                fromDate = convertToStringDateTimeForHistory(state.value.fromDate),
                toDate = convertToStringDateTimeForHistory(state.value.toDate),
                limit = limit
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        isLoadingHistory = false,
                    )
                        .updateState()
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        _state.value.copy(
                            isLoading = false,
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        isLoadingHistory = false,
                        //TODO
                    )
                        .updateState()
                }
            }
        }
    }

    private fun clearSensors() {
        state.value.device?.let { device ->
            _state.value.copy(
                isLoading = true,
            )
                .updateState()

            var count = 0
            /*viewModelScope.launch {
                device.params.forEach { param ->
                    when (val result = deleteParamUseCase(
                        token = state.value.token,
                        id = param.id
                    )) {
                        is ResultWork.Error -> {
                            _state.value.copy(
                                message = result.error.asUiText()
                            )
                                .updateState()
                            mutex.withLock {
                                count++
                            }
                        }

                        is ResultWork.Success -> {
                            mutex.withLock {
                                count++
                            }
                            if (count == device.params.size) {
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
                }
            }*/
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
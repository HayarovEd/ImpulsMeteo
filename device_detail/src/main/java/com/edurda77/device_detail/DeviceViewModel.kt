package com.edurda77.device_detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.edurda77.domain.model.Param
import com.edurda77.domain.model.WebSocketMessage
import com.edurda77.domain.usecase.ClearHistoryDeviceUseCase
import com.edurda77.domain.usecase.CloseWebsocketUseCase
import com.edurda77.domain.usecase.DeleteDeviceUseCase
import com.edurda77.domain.usecase.DeviceByIdUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.HistoryUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.UnitsUseCase
import com.edurda77.domain.usecase.UpdateDeviceUseCase
import com.edurda77.domain.usecase.UpdateFavoriteUseCase
import com.edurda77.domain.usecase.UpdateNotificationsDeviceUseCase
import com.edurda77.domain.usecase.UpdateParamUseCase
import com.edurda77.domain.usecase.WebSocketUseCase
import com.edurda77.domain.usecase.WsMessageFactory
import com.edurda77.domain.usecase.WsMessageFactory.updateParamInDevice
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
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

class DeviceViewModel(
    private val deviceByIdUseCase: DeviceByIdUseCase,
    private val loggedUserUseCase: LoggedUserUseCase,
    savedStateHandle: SavedStateHandle,
    private val updateNotificationsDeviceUseCase: UpdateNotificationsDeviceUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val updateDeviceUseCase: UpdateDeviceUseCase,
    private val unitsUseCase: UnitsUseCase,
    private val updateParamUseCase: UpdateParamUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val closeWebsocketUseCase: CloseWebsocketUseCase,
    private val historyUseCase: HistoryUseCase,
    private val deleteDeviceUseCase: DeleteDeviceUseCase,
    private val clearHistoryDeviceUseCase: ClearHistoryDeviceUseCase,
) : ViewModel() {
    private val deviceId = savedStateHandle.toRoute<NavigationRoute.Device>().id
    private var _state = MutableStateFlow(DeviceState())
    val state = _state
        .onStart {
            loadUserData()
            updateFromWs()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DeviceState()
        )


    private val _eventFlow = Channel<UiDeviceEvents>()
    val eventFlow = _eventFlow.receiveAsFlow()

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

            is DeviceEvent.UpdateNotifications -> {
                state.value.authUser?.let { user ->
                    viewModelScope.launch {
                        when (val result = updateNotificationsDeviceUseCase.invoke(
                            notificationsParam = event.notificationsParam.map { it.copy(userId = user.id) },
                            value = event.value,
                            deviceId = deviceId,
                            userId = user.id
                        )) {
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
                                state.value.device?.let { device ->
                                    _state.value.copy(
                                        device = device.copy(
                                            notificationDevice = result.data
                                        )
                                    )
                                        .updateState()
                                }
                            }
                        }
                    }
                }

            }

            is DeviceEvent.UpdateSelectedGroups -> {
                val selectedGroups = state.value.selectedGroups
                if (selectedGroups.contains(event.groupDevices)) {
                    _state.value.copy(
                        selectedGroups = selectedGroups - event.groupDevices
                    )
                        .updateState()
                } else {
                    _state.value.copy(
                        selectedGroups = selectedGroups + event.groupDevices
                    )
                        .updateState()
                }
            }

            is DeviceEvent.UpdateDevice -> {
                updateDevice(
                    key = event.key,
                    name = event.name,
                    updateRate = event.frequency
                )
            }

            DeviceEvent.BackStartGroups -> {
                state.value.device?.let { device ->
                    _state.value.copy(
                        selectedGroups = device.groups,
                    )
                        .updateState()
                }
            }

            is DeviceEvent.UpdateParam -> {
                updateParam(event.param)
            }

            is DeviceEvent.WorkWithFavorite -> {
                updateFavorite()
            }

            DeviceEvent.DeleteDevice -> {
                deleteDevice()
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


    private fun updateFavorite() {
        viewModelScope.launch {
            state.value.authUser?.let { user ->
                when (val result =
                    updateFavoriteUseCase.invoke(
                        deviceId = deviceId,
                        favorites = user.favorites
                    )
                ) {
                    is ResultWork.Error -> {
                        if (result.error is DataError.TokenError) {
                            _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                        } else {
                            _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        }
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            authUser = user.copy(favorites = result.data)
                        )
                            .updateState()
                    }
                }
            }
        }
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
                    val notificationDevice = state.value.authUser?.let { user ->
                        user.devices.firstOrNull { it.id == deviceId }?.notificationDevice
                    }
                    _state.value.copy(
                        device = result.data.copy(notificationDevice = notificationDevice),
                        selectedGroups = result.data.groups,
                        isLoading = false
                    )
                        .updateState()
                }
            }
        }
    }

    private fun loadGroupsAndUnits() {
        viewModelScope.launch {
            val resultGroupsDiff = async { devicesGroupsUseCase.invoke() }
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

    private fun loadHistory(limit: Int) {
        _state.value.copy(
            isLoadingHistory = true,
            histories = emptyMap(),
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
                        histories = result.data
                    )
                        .updateState()
                }
            }
        }
    }

    private fun clearSensors() {
        state.value.device?.let { device ->
            viewModelScope.launch {
                when (val result = clearHistoryDeviceUseCase.invoke(
                    ids = device.params.map { it.id }
                )) {
                    is ResultWork.Error -> {
                        if (result.error is DataError.TokenError) {
                            _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                        } else {
                            _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        }
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            histories = emptyMap()
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    private fun updateParam(param: Param) {
        state.value.device?.let { device ->
            viewModelScope.launch {
                when (val result = updateParamUseCase.invoke(
                    param = param
                )) {
                    is ResultWork.Error -> {
                        if (result.error is DataError.TokenError) {
                            _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                        } else {
                            _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        }
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            device = device.copy(
                                params = device.params.map { prm ->
                                    if (prm.id == result.data.id) result.data else prm
                                }
                            ),
                        )
                            .updateState()
                    }
                }
            }
        }

    }

    private fun updateDevice(
        key: String,
        name: String,
        updateRate: String,
    ) {
        updateRate.toIntOrNull()?.let { ur ->
            viewModelScope.launch {
                when (val result = updateDeviceUseCase.invoke(
                    deviceId = deviceId,
                    key = key,
                    name = name,
                    updateRate = ur,
                    groups = state.value.selectedGroups
                )) {
                    is ResultWork.Error -> {
                        if (result.error is DataError.TokenError) {
                            _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                        } else {
                            _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                        }
                    }

                    is ResultWork.Success -> {
                        _state.value.copy(
                            device = result.data.copy(
                                notificationDevice = state.value.device?.notificationDevice
                            )
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    private fun deleteDevice(
    ) {
        viewModelScope.launch {
            when (val result = deleteDeviceUseCase.invoke(
                deviceId = deviceId,
            )) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                    } else {
                        _eventFlow.send(UiDeviceEvents.OnError(result.error.asUiText()))
                    }
                }

                is ResultWork.Success -> {
                    _eventFlow.send(UiDeviceEvents.BackUpNavigationEvent)
                }
            }
        }
    }


    private fun updateFromWs() {
        viewModelScope.launch {
            webSocketUseCase.invoke().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {}
                    is ResultWork.Success -> {
                        when (val successResult = collector.data) {

                            is WebSocketMessage.DeviceUpdate -> {
                                if (deviceId == successResult.device.id) {
                                    _state.value.copy(
                                        device = successResult.device.copy(
                                            notificationDevice = state.value.device?.notificationDevice
                                        )
                                    )
                                        .updateState()
                                }
                            }

                            is WebSocketMessage.FavoriteUpdate -> {
                                state.value.authUser?.let { user ->
                                    _state.value.copy(
                                        authUser = user.copy(
                                            favorites = successResult.favorites
                                        ),
                                    )
                                        .updateState()
                                }
                            }

                            is WebSocketMessage.ParamDataUpdate -> {
                                if (deviceId == successResult.device.id) {
                                    _state.value.copy(
                                        device = successResult.device.copy(
                                            notificationDevice = state.value.device?.notificationDevice
                                        )
                                    )
                                        .updateState()
                                }
                            }

                            is WebSocketMessage.ParamUpdate -> {
                                state.value.device?.let { device ->
                                    _state.value.copy(
                                        device = updateParamInDevice(
                                            device = device,
                                            newParam = successResult.param
                                        )
                                    )
                                        .updateState()
                                }

                            }

                            is WebSocketMessage.UserDelete -> {
                                if (state.value.authUser?.id == successResult.id) {
                                    viewModelScope.launch {
                                        _eventFlow.send(UiDeviceEvents.LoginNavigationEvent)
                                    }
                                }
                            }

                            is WebSocketMessage.UserUpdate -> {
                                state.value.authUser?.let { user ->
                                    _state.value.copy(
                                        authUser = WsMessageFactory.updateAuthUser(
                                            authUser = user,
                                            newUser = successResult.user
                                        ),
                                    )
                                        .updateState()
                                }
                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }


    private fun DeviceState.updateState() {
        _state.update {
            this
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            closeWebsocketUseCase.invoke()
        }
    }
}
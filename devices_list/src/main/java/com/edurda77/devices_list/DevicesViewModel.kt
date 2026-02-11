package com.edurda77.devices_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.domain.model.newModels.Device
import com.edurda77.domain.model.newModels.GroupDevice
import com.edurda77.domain.model.newModels.WebSocketMessage
import com.edurda77.domain.usecase.AddDeviceUseCase
import com.edurda77.domain.usecase.CloseWebsocketUseCase
import com.edurda77.domain.usecase.DevicesGroupsUseCase
import com.edurda77.domain.usecase.LogOffUseCase
import com.edurda77.domain.usecase.LoggedUserUseCase
import com.edurda77.domain.usecase.UpdateFavoriteUseCase
import com.edurda77.domain.usecase.WebSocketUseCase
import com.edurda77.domain.usecase.WsMessageFactory
import com.edurda77.domain.utils.DEVICES_CREATE_LABEL
import com.edurda77.domain.utils.DataError
import com.edurda77.domain.utils.ResultWork
import com.edurda77.download_install.refresher.Refresher
import com.edurda77.download_install.utils.APK_EXT
import com.edurda77.download_install.utils.DownloadStatus
import com.edurda77.download_install.utils.ResultDownloadWork
import com.edurda77.download_install.utils.asUiResultText
import com.edurda77.resources.uikit.asUiText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

const val DOWNLOAD_VERSION_URL =
    "https://apps.kvadroks.ru/api/links/fd383935-944f-4a81-bb06-d31da0554d4a/version"

const val DOWNLOAD_FILE_URL =
    "https://apps.kvadroks.ru/api/links/fd383935-944f-4a81-bb06-d31da0554d4a/file"

class DevicesViewModel(
    private val loggedUserUseCase: LoggedUserUseCase,
    private val logoffUseCase: LogOffUseCase,
    private val addDeviceUseCase: AddDeviceUseCase,
    private val devicesGroupsUseCase: DevicesGroupsUseCase,
    private val webSocketUseCase: WebSocketUseCase,
    private val closeWebsocketUseCase: CloseWebsocketUseCase,
    private val updateFavoriteUseCase: UpdateFavoriteUseCase,
    private val refresher: Refresher,

    ) : ViewModel() {
    private var _state = MutableStateFlow(DevicesState())
    val state = _state
        .onStart {
            // loadLocalData()
            //loadUpdateData()
            checkEnableUpdates()
            loadUserData()
            updateFromWs()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            DevicesState()
        )

    private val _eventFlow = MutableSharedFlow<UiDevicesEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: DevicesEvent) {
        when (event) {
            DevicesEvent.Logoff -> {
                viewModelScope.launch {
                    logoffUseCase.invoke()
                    _eventFlow.emit(UiDevicesEvents.LoginNavigationEvent)
                }
            }

            is DevicesEvent.OnSearch -> {
                viewModelScope.launch {
                    _state.value.copy(
                        query = event.query
                    )
                        .updateState()
                }
            }

            DevicesEvent.Refresh -> {
                _state.value.copy(
                    isLoading = true,
                )
                    .updateState()
                loadUserData()
            }

            is DevicesEvent.SelectGroup -> {
                _state.value.copy(
                    numberSelectedGroup = event.index
                )
                    .updateState()
            }

            DevicesEvent.ShowSearchField -> {
                _state.value.copy(
                    isShowSearch = !state.value.isShowSearch
                )
                    .updateState()
            }

            is DevicesEvent.OnInsertDevice -> {
                viewModelScope.launch {
                    insertDevice(
                        name = event.name,
                        key = event.key,
                        frequency = event.frequency,
                        groups = event.groups
                    )
                }
            }

            is DevicesEvent.UpdateSelectedGroups -> {
                val updatedGroups = state.value.selectedGroups.toMutableList()
                if (state.value.selectedGroups.contains(event.groupDevices)) {
                    updatedGroups.remove(event.groupDevices)
                } else {
                    updatedGroups.add(event.groupDevices)
                }
                _state.value.copy(
                    selectedGroups = updatedGroups
                )
                    .updateState()
            }

            DevicesEvent.ClearSelectedGroups -> {
                _state.value.copy(
                    selectedGroups = emptyList()
                )
                    .updateState()
            }

            DevicesEvent.OnCloseWebSocket -> {
                viewModelScope.launch {
                    closeWebsocketUseCase.invoke()
                }
            }

            is DevicesEvent.WorkWithFavorite -> {
                updateFavorite(event.device)
            }

            is DevicesEvent.OnDeleteDevice -> {

            }

            DevicesEvent.UpdateApp -> updateApk()
            DevicesEvent.SortDevicesByStatus -> {
                viewModelScope.launch {
                    _state.value.copy(
                        isSorted = !state.value.isSorted,
                    )
                        .updateState()
                }
            }
        }
    }


    private suspend fun insertDevice(
        name: String,
        key: String,
        frequency: String,
        groups: List<GroupDevice>
    ) {
        frequency.toIntOrNull()?.let { fr ->
            when (val result = addDeviceUseCase.invoke(
                name = name,
                key = key,
                frequency = fr,
                groups = groups,
            )) {
                is ResultWork.Error -> {
                    _state.value.copy(
                        message = result.error.asUiText()
                    )
                        .updateState()
                }

                is ResultWork.Success -> {
                    state.value.authUser?.let { user ->
                        _state.value.copy(
                            authUser = user.copy(
                                devices = user.devices + result.data
                            ),
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    private suspend fun loadGroups() {
        when (val result = devicesGroupsUseCase.invoke()) {
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

    private fun checkEnableUpdates() {
        viewModelScope.launch {
            when (val result =
                refresher.getLastVersion(DOWNLOAD_VERSION_URL)) {
                is ResultDownloadWork.Error -> {
                    Log.d(
                        "TEST UPDATE METEO",
                        "error check update ${result.error.asUiResultText()}"
                    )
                }

                is ResultDownloadWork.Success -> {
                    _state.value.copy(
                        release = result.data
                    )
                        .updateState()
                    Log.d("TEST UPDATE METEO", "name ${result.data.name}")
                    Log.d("TEST UPDATE METEO", "version ${result.data.lastVersion}")
                    val currentVersion = refresher.getCurrentVersion()
                    currentVersion?.let {
                        _state.value.copy(
                            enableUpdate = currentVersion < result.data.lastVersion
                        )
                            .updateState()
                    }
                }
            }
        }
    }

    private fun updateApk() {
        viewModelScope.launch {
            state.value.release?.let { release ->
                refresher.updateInBackground(
                    url = DOWNLOAD_FILE_URL,
                    downloadedFileName = "${release.name}-${release.lastVersion}.$APK_EXT"
                ).collect { collector ->
                    when (collector) {
                        is DownloadStatus.Error -> {
                            Log.d(
                                "TEST UPDATE METEO",
                                "error update ${collector.error.asUiResultText()}"
                            )
                        }

                        is DownloadStatus.InProgress -> {
                            _state.value.copy(
                                percentUpdate = collector.percentage
                            )
                                .updateState()
                        }

                        DownloadStatus.Started -> {
                            Log.d("TEST UPDATE METEO", "update started")
                            _state.value.copy(
                                isUpdating = true
                            )
                                .updateState()
                        }

                        DownloadStatus.Success -> {
                            _state.value.copy(
                                isUpdating = false
                            )
                                .updateState()
                        }
                    }
                }
            }
        }
    }

    private fun loadUserData() {

        viewModelScope.launch {
            when (val result = loggedUserUseCase.invoke()) {
                is ResultWork.Error -> {
                    if (result.error is DataError.TokenError) {
                        _eventFlow.emit(UiDevicesEvents.LoginNavigationEvent)
                    } else {
                        _state.value.copy(
                            isLoading = false,
                            message = result.error.asUiText()
                        )
                            .updateState()
                    }
                }

                is ResultWork.Success -> {
                    _state.value.copy(
                        authUser = result.data,
                        isLoading = false,
                    )
                        .updateState()
                    if (result.data.permissions.map { it.name }.contains(DEVICES_CREATE_LABEL)) {
                        loadGroups()
                    }
                }
            }
        }
    }

    private fun updateFavorite(device: Device) {
        viewModelScope.launch {
            state.value.authUser?.let { user ->
                val favorites = user.favorites.map { fv ->
                    if (device.id == fv.deviceId) {
                        fv.copy(
                            isUpdating = true
                        )
                    } else fv
                }
                _state.value.copy(
                    authUser = user.copy(favorites = favorites)
                )
                    .updateState()
                when (val result =
                    updateFavoriteUseCase.invoke(
                        deviceId = device.id,
                        favorites = favorites
                    )
                ) {
                    is ResultWork.Error -> {
                        _state.value.copy(
                            message = result.error.asUiText()
                        )
                            .updateState()
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

    private fun updateFromWs() {
        viewModelScope.launch {
            webSocketUseCase.invoke().collect { collector ->
                when (collector) {
                    is ResultWork.Error -> {}
                    is ResultWork.Success -> {
                        when (val successResult = collector.data) {
                            is WebSocketMessage.DeviceCreate -> {
                                loadUserData()
                            }

                            is WebSocketMessage.DeviceDelete -> {
                                state.value.authUser?.let { user ->
                                    _state.value.copy(
                                        authUser = user.copy(
                                            devices = WsMessageFactory.deleteDevice(
                                                devices = user.devices,
                                                id = successResult.id
                                            )
                                        ),
                                    )
                                        .updateState()
                                }
                            }

                            is WebSocketMessage.DeviceUpdate -> {
                                state.value.authUser?.let { user ->
                                    _state.value.copy(
                                        authUser = user.copy(
                                            devices = WsMessageFactory.updateDevice(
                                                devices = user.devices,
                                                device = successResult.device
                                            )
                                        ),
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
                                state.value.authUser?.let { user ->
                                    _state.value.copy(
                                        authUser = user.copy(
                                            devices = WsMessageFactory.updateDevice(
                                                devices = user.devices,
                                                device = successResult.device
                                            )
                                        ),
                                    )
                                        .updateState()
                                }
                            }

                            is WebSocketMessage.ParamUpdate -> {
                                state.value.authUser?.let { user ->
                                    _state.value.copy(
                                        authUser = user.copy(
                                            devices = WsMessageFactory.updateParam(
                                                devices = user.devices,
                                                newParam = successResult.param
                                            )
                                        ),
                                    )
                                        .updateState()
                                }
                            }

                            is WebSocketMessage.UserCreate -> {}
                            is WebSocketMessage.UserDelete -> {
                                viewModelScope.launch {
                                    state.value.authUser?.let { user ->
                                        if (user.id == successResult.id) {
                                            logoffUseCase.invoke()
                                            _eventFlow.emit(UiDevicesEvents.LoginNavigationEvent)
                                        }
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
                        }
                    }
                }
            }
        }
    }


    private fun DevicesState.updateState() {
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
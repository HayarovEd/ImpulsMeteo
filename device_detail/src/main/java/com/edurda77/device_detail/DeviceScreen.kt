package com.edurda77.device_detail

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DeviceScreen(
    modifier: Modifier = Modifier,
    // onGoToLogin: () -> Unit,
    // onGoToDevice: (Int) -> Unit,
    viewModel: DeviceViewModel = hiltViewModel(),
    //  configuration: Configuration,=
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    Log.d("TEST DEVECE DETAIL SCREEN", "device ${state.value.device}")
    state.value.device?.notifications?.forEach {
        Log.d("TEST DEVECE DETAIL SCREEN", "notifications $it")
    }
}

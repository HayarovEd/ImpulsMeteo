package com.edurda77.list_camers_screen

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.edurda77.devices_list.DevicesEvent
import com.edurda77.devices_list.DevicesViewModel
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.ItemDevice
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiTextField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevicesScreen(
    modifier: Modifier = Modifier,
    onGoToLogin: () -> Unit,
    onGoToDevice: (Int) -> Unit,
    viewModel: DevicesViewModel = hiltViewModel(),
    configuration: Configuration,
    bottomBarContent: @Composable () -> Unit = {},
) {
    val state = viewModel.state.collectAsStateWithLifecycle()
    val onEvent = viewModel::onEvent
    val localDensity = LocalDensity.current
    val screenWidth = configuration.screenWidthDp.dp
    val listState = rememberLazyListState()
    val pagerState =
        rememberPagerState(
            pageCount = { state.value.devices.size }
        )
    val scope = rememberCoroutineScope()
    /*LaunchedEffect(state.value.devices.size) {
        if (state.value.devices.isNotEmpty()) {
              listState.animateScrollToItem(state.value.devices.size  / 2 - 1)
              pagerState.animateScrollToPage(state.value.devices.size  / 2)
        }
    }*/

    LaunchedEffect(pagerState.currentPage) {
        Log.d("TEST DEVICES SCREEN", "currentPage ${pagerState.currentPage}")
        if (state.value.devices.isNotEmpty()) {
            onEvent(DevicesEvent.SelectGroup(pagerState.currentPage))
            if (pagerState.currentPage > 0 && pagerState.currentPage != listState.layoutInfo.totalItemsCount - 1) {
                listState.animateScrollToItem(pagerState.currentPage - 1)
            }
        }
    }
    val isShowDialogLogOff = remember { mutableStateOf(false) }
    BackHandler {}
    /* if (isShowDialogLogOff.value) {
         UiAlertDialog(
             title = stringResource(id = R.string.sure_exit),
             onClickConfirm = {
                 isShowDialogLogOff.value = false
                 onEvent(MonitorsEvent.Logoff)
                 onGoToLogin()
             },
             onClickCancel = {
                 isShowDialogLogOff.value = false
             }
         )
     }*/

    UiBaseScaffold(
        message = state.value.message,
        topBarContent = {
            Column(
                modifier = modifier
                    .padding(top = 50.dp, start = 15.dp, end = 15.dp)
                    .fillMaxWidth(),
            ) {
                if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE && !state.value.isShowSearch) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (state.value.isShowSearch) Arrangement.spacedBy(0.dp) else Arrangement.SpaceBetween
                    ) {
                        val modifierByVisibilitySearch =
                            if (state.value.isShowSearch) modifier.weight(1f) else modifier
                        UiIconButton(
                            modifier = modifierByVisibilitySearch,
                            icon = if (state.value.isShowSearch) ImageVector.vectorResource(id = R.drawable.baseline_search_off_24) else ImageVector.vectorResource(
                                id = R.drawable.baseline_search_24
                            ),
                            onClick = {
                                onEvent(DevicesEvent.ShowSearchField)
                                onEvent(DevicesEvent.OnSearch(""))
                            }
                        )
                        DevicesSelectorGroup(
                            modifier = modifier.weight(5f),
                            listState = listState,
                            devices = state.value.devices,
                            numberSelectedGroup = state.value.numberSelectedGroup,
                            pagerState = pagerState,
                            scope = scope,
                            screenWidth = screenWidth,
                            onClick = {
                                DevicesEvent.SelectGroup(it)
                            }
                        )
                        UiIconButton(
                            modifier = modifierByVisibilitySearch,
                            icon = ImageVector.vectorResource(id = R.drawable.baseline_logout_24),
                            onClick = { isShowDialogLogOff.value = true }
                        )
                    }
                } else {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = if (state.value.isShowSearch) Arrangement.spacedBy(0.dp) else Arrangement.SpaceBetween
                    ) {
                        val modifierByVisibilitySearch =
                            if (state.value.isShowSearch) modifier.weight(1f) else modifier
                        UiIconButton(
                            modifier = modifierByVisibilitySearch,
                            icon = if (state.value.isShowSearch) ImageVector.vectorResource(id = R.drawable.baseline_search_off_24) else ImageVector.vectorResource(
                                id = R.drawable.baseline_search_24
                            ),
                            onClick = {
                                onEvent(DevicesEvent.ShowSearchField)
                                onEvent(DevicesEvent.OnSearch(""))
                            }
                        )
                        AnimatedVisibility(
                            modifier = modifier.weight(7f),
                            visible = state.value.isShowSearch,
                            enter = slideInVertically {
                                with(localDensity) { -40.dp.roundToPx() }
                            } + expandVertically(
                                expandFrom = Alignment.Top
                            ) + fadeIn(
                                initialAlpha = 0.3f
                            ),
                            exit = slideOutVertically() + shrinkVertically() + fadeOut()
                        ) {
                            UiTextField(
                                content = state.value.query,
                                label = stringResource(id = R.string.search),
                                onClickContent = {
                                    onEvent(DevicesEvent.OnSearch(it))
                                })
                        }
                        UiIconButton(
                            modifier = modifierByVisibilitySearch,
                            icon = ImageVector.vectorResource(id = R.drawable.baseline_logout_24),
                            onClick = { isShowDialogLogOff.value = true }
                        )
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    DevicesSelectorGroup(
                        listState = listState,
                        devices = state.value.devices,
                        numberSelectedGroup = state.value.numberSelectedGroup,
                        onClick = {
                            DevicesEvent.SelectGroup(it)
                        },
                        scope = scope,
                        pagerState = pagerState,
                        screenWidth = screenWidth,
                    )
                }
            }
        },
        bottomBarContent = bottomBarContent,
        content = { paddings ->
            PullToRefreshBox(
                modifier = modifier.padding(paddings),
                isRefreshing = state.value.isLoading,
                onRefresh = { onEvent(DevicesEvent.Refresh) },
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
                if (state.value.devices.isNotEmpty() && !state.value.isLoading) {
                    HorizontalPager(
                        modifier = modifier
                            .fillMaxSize()
                            .padding(15.dp),
                        state = pagerState,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        //  val index = page % state.value.devices.size
                        val currentDevices = state.value.devices.values.toList()[page]
                        val cellsCount =
                            if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 2 else 1
                        LazyVerticalStaggeredGrid(
                            modifier = Modifier
                                .fillMaxWidth(),
                            columns = StaggeredGridCells.Fixed(cellsCount),
                            verticalItemSpacing = 5.dp,
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            items(currentDevices) { device ->
                                ItemDevice(
                                    modifier = modifier,
                                    device = device,
                                    configuration = configuration,
                                    /* authToken = state.value.user?.authToken ?: "",
                                     onClick = {
                                         onGoToCamera(device.mid)
                                     }*/
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun DevicesSelectorGroup(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    devices: Map<GroupDevices, List<Device>>,
    numberSelectedGroup: Int,
    onClick: (Int) -> Unit,
    scope: CoroutineScope,
    pagerState: PagerState,
    screenWidth: Dp,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        state = listState,
    ) {
        items(
            count = devices.size
        ) {
            //val index = it % monitors.size
            Box(
                modifier = modifier
                    .shadow(elevation = if (it == numberSelectedGroup) 10.dp else 0.dp)
                    .width(screenWidth / 3)
                    .clip(shape = RoundedCornerShape(3.dp))
                    .background(
                        color = if (it == numberSelectedGroup) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.3f
                        )
                    )
                    .clickable {
                        onClick(it)
                        scope.launch {
                            if (it != 0 && it != listState.layoutInfo.totalItemsCount - 1)
                                listState.scrollToItem(it - 1)
                            pagerState.animateScrollToPage(it)
                        }
                    }
                    .padding(vertical = 3.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = modifier
                        .basicMarquee(),
                    text = devices.keys.toList()[it].name,
                    style = Typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
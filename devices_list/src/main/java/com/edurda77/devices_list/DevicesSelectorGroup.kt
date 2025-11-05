package com.edurda77.devices_list

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.Device
import com.edurda77.domain.model.GroupDevices
import com.edurda77.domain.model.Param
import com.edurda77.domain.utils.FAVORITE
import com.edurda77.resources.R
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DevicesSelectorGroup(
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
        horizontalArrangement = Arrangement.spacedBy(15.dp),
        state = listState,
    ) {
        items(
            count = devices.size
        ) {
            //val index = it % monitors.size
            val groupName =
                if (devices.keys.toList()[it].name == FAVORITE) stringResource(R.string.favorite) else devices.keys.toList()[it].name
            Box(
                modifier = modifier
                    .width(screenWidth / 3)
                    .border(
                        border = BorderStroke(
                            width = 1.dp,
                            if (it == numberSelectedGroup) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(
                        color = if (it == numberSelectedGroup) MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.2f
                        ) else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                    )
                    .clickable {
                        onClick(it)
                        scope.launch {
                            if (it != 0 && it != listState.layoutInfo.totalItemsCount - 1)
                                listState.scrollToItem(it - 1)
                            pagerState.animateScrollToPage(it)
                        }
                    }
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    modifier = modifier
                        .basicMarquee(),
                    text = groupName,
                    style = Typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = if (it == numberSelectedGroup) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                )
            }
        }
    }
}

@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun DevicesSelectorGroupView1() {
    ImpulsMeteoTheme {
        DevicesSelectorGroup(
            listState = rememberLazyListState(),
            devices = mapOf(
                Pair(
                    GroupDevices(
                        id = 0,
                        name = "group 1"
                    ),
                    listOf(
                        Device(
                            id = 0,
                            name = "Auto",
                            key = "1223",
                            status = true,
                            video = null,
                            updatedAt = "12-03-2025",
                            groups = listOf(
                                GroupDevices(
                                    id = 1,
                                    name = "Perm"
                                )
                            ),
                            params = listOf(
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                ),
                                Param(
                                    id = 0,
                                    idUnit = 1,
                                    name = "Temp",
                                    label = "tmp",
                                    value = 12.0,
                                    color = "#808080",
                                    classIcon = "wi wi-thermometer-exterior",
                                    isHidden = false,
                                    idDevice = 0
                                )
                            ),
                            isFavorite = true,
                            statusNotifications = true,
                        )
                    ),
                ),
                Pair(
                    GroupDevices(
                        id = 1,
                        name = "group 2"
                    ),
                    emptyList()
                ),
                Pair(
                    GroupDevices(
                        id = 2,
                        name = "group 3"
                    ),
                    emptyList()
                )
            ),
            numberSelectedGroup = 0,
            onClick = {},
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState(
                pageCount = { 2 }
            ),
            screenWidth = 1000.dp
        )
    }
}

@Preview
@Composable
private fun DevicesSelectorGroupView2() {
    ImpulsMeteoTheme {
        DevicesSelectorGroup(
            listState = rememberLazyListState(),
            devices = mapOf(
                Pair(
                    GroupDevices(
                        id = 0,
                        name = "group 1"
                    ),
                    emptyList()
                ),
                Pair(
                    GroupDevices(
                        id = 1,
                        name = "group 2"
                    ),
                    emptyList()
                ),
                Pair(
                    GroupDevices(
                        id = 2,
                        name = "group 3"
                    ),
                    emptyList()
                )
            ),
            numberSelectedGroup = 0,
            onClick = {},
            scope = rememberCoroutineScope(),
            pagerState = rememberPagerState(
                pageCount = { 2 }
            ),
            screenWidth = 1000.dp
        )
    }
}
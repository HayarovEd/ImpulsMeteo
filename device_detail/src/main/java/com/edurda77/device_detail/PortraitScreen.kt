package com.edurda77.device_detail

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.SingleDevice
import com.edurda77.resources.R
import com.edurda77.resources.theme.Typography
import com.edurda77.resources.uikit.UiBaseScaffold
import com.edurda77.resources.uikit.UiDateContent
import com.edurda77.resources.uikit.UiIconButton
import com.edurda77.resources.uikit.UiText

@Composable
fun PortraitScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    configuration: Configuration,
    message: UiText?,
    dateFrom: String,
    dateTo: String,
    isOpenFilter: Boolean,
    openFromDateDialog: () -> Unit,
    openToDateDialog: () -> Unit,
    openFilter: (Boolean) -> Unit,
    isLoading: Boolean,
    device: SingleDevice?,
    currentLimit: MutableIntState,
    limits: List<Int>,
    expandedLimits: Boolean,
    onClickExpandedLimit: () -> Unit,
    onClickLimit: (Int) -> Unit,
) {
    val localDensity = LocalDensity.current
    val offsetXDropDownMenu = remember { mutableStateOf(0.dp) }

    UiBaseScaffold(
        message = message,
        configuration = configuration,
        topBarContent = {
            if (!isLoading) {
                Column(
                    modifier = modifier
                        .padding(top = 50.dp, start = 15.dp, end = 15.dp)
                        .fillMaxWidth(),
                ) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        UiIconButton(
                            modifier = modifier,
                            icon = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
                            onClick = onBackClick
                        )
                        Text(
                            modifier = modifier,
                            text = device?.name ?: "",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.titleLarge,
                        )
                        Spacer(modifier = modifier.weight(1f))
                        UiIconButton(
                            modifier = modifier,
                            icon = ImageVector.vectorResource(id = R.drawable.outline_notifications_24),
                            onClick = {
                                ///////////
                            }
                        )
                        UiIconButton(
                            modifier = modifier,
                            icon = ImageVector.vectorResource(id = R.drawable.baseline_edit_24),
                            onClick = {
                                ///////////
                            }
                        )
                        Text(
                            modifier = modifier,
                            text = if (device?.status == true) stringResource(R.string.online) else stringResource(
                                R.string.offline
                            ),
                            color = if (device?.status == true) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.error,
                            style = Typography.bodyLarge,
                        )
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.updated_data),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                            Text(
                                modifier = modifier,
                                text = device?.updatedAt ?: "",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                        }
                        Column {
                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.key_l),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                            Text(
                                modifier = modifier,
                                text = device?.key ?: "",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                        }
                        Column {
                            Text(
                                modifier = modifier,
                                text = stringResource(R.string.update),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                            Spacer(modifier = modifier.height(3.dp))
                            Text(
                                modifier = modifier,
                                text = device?.frequency.toString(),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                style = Typography.bodyLarge,
                            )
                        }
                    }
                    Spacer(modifier = modifier.height(10.dp))
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .clickable(
                                onClick = { openFilter(!isOpenFilter) }
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = modifier,
                            text = stringResource(R.string.filter),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = Typography.bodyLarge,
                        )
                        UiIconButton(
                            icon = if (isOpenFilter) ImageVector.vectorResource(R.drawable.baseline_arrow_drop_up_24) else ImageVector.vectorResource(
                                R.drawable.baseline_arrow_drop_down_24
                            ),
                            onClick = { openFilter(!isOpenFilter) }
                        )
                    }
                    AnimatedVisibility(
                        modifier = modifier,
                        visible = isOpenFilter,
                        enter = slideInVertically {
                            with(localDensity) { -40.dp.roundToPx() }
                        } + expandVertically(
                            expandFrom = Alignment.Top
                        ) + fadeIn(
                            initialAlpha = 0.3f
                        ),
                        exit = slideOutVertically() + shrinkVertically() + fadeOut()
                    ) {
                        Row(
                            modifier = modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            UiDateContent(
                                modifier = modifier.weight(1f),
                                title = stringResource(R.string.date_from),
                                content = dateFrom,
                                onClick = openFromDateDialog
                            )
                            UiDateContent(
                                modifier = modifier.weight(1f),
                                title = stringResource(R.string.date_to),
                                content = dateTo,
                                onClick = openToDateDialog
                            )
                            UiDateContent(
                                modifier = modifier
                                    .weight(1f)
                                    .basicMarquee()
                                    .onGloballyPositioned { coordinates ->
                                        offsetXDropDownMenu.value =
                                            with(localDensity) { coordinates.positionInRoot().x.toDp() }
                                    },
                                title = stringResource(R.string.count_records),
                                content = currentLimit.intValue.toString(),
                                icon = null,
                                onClick = onClickExpandedLimit,
                            )
                            DropdownMenu(
                                modifier = modifier,
                                offset = DpOffset(x = offsetXDropDownMenu.value, y = 0.dp),
                                expanded = expandedLimits,
                                onDismissRequest = onClickExpandedLimit
                            ) {
                                limits.forEachIndexed { index, limit ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = limit.toString(),
                                                style = Typography.labelSmall,
                                            )
                                        }, onClick = {
                                            onClickExpandedLimit()
                                            onClickLimit(index)
                                        })
                                }
                            }
                            IconButton(onClick = {
                                onClickExpandedLimit()
                            }) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_drop_down_24),
                                    contentDescription = "",
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        content = {

        }
    )
}
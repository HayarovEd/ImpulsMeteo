package com.edurda77.directories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.GroupDevice


@Composable
fun DirectoryScreenGroups(
    modifier: Modifier = Modifier,
    isEnableUpdate: Boolean,
    groups: List<GroupDevice>,
    cellsCount: Int,
    onDeleteClick: (String) -> Unit,
    onUpdateClick: (GroupDevice) -> Unit,
    titleDelete: String
) {
    if (groups.isNotEmpty()) {
        LazyVerticalStaggeredGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = StaggeredGridCells.Fixed(cellsCount),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(groups) { group ->
                ItemDevicesGroupDirectory(
                    title = group.name,
                    isEnabledUpdate = isEnableUpdate,
                    onDeleteClick = { onDeleteClick(group.id) },
                    titleDelete = titleDelete,
                    onUpdateClick = { name ->
                        onUpdateClick(
                            GroupDevice(
                                id = group.id,
                                name = name
                            )
                        )
                    }
                )
            }
        }
    }
}
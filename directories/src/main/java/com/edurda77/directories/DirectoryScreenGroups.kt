package com.edurda77.directories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.GroupDevices


@Composable
fun DirectoryScreenGroups(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isEnableUpdate: Boolean,
    groups: List<GroupDevices>,
    cellsCount: Int,
    onDeleteClick: (Int) -> Unit,
    titleDelete: String
) {
    if (groups.isNotEmpty() && !isLoading) {
        LazyVerticalStaggeredGrid(
            modifier = modifier
                .fillMaxSize()
                .padding(15.dp),
            columns = StaggeredGridCells.Fixed(cellsCount),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(groups) { group ->
                ItemDirectory(
                    title = group.name,
                    isEnabledUpdate = isEnableUpdate,
                    onDeleteClick = { onDeleteClick(group.id) },
                    titleDelete = titleDelete
                )
            }
        }
    }
}
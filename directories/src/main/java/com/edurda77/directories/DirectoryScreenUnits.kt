package com.edurda77.directories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.UnitMeteo


@Composable
fun DirectoryScreenUnits(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    isEnableUpdate: Boolean,
    units: List<UnitMeteo>,
    cellsCount: Int,
    onDeleteClick: (Int) -> Unit,
    onUpdateClick: (Int, String, String) -> Unit,
    titleDelete: String
) {
    if (units.isNotEmpty() && !isLoading) {
        LazyVerticalStaggeredGrid(
            modifier = modifier
                .fillMaxSize(),
            columns = StaggeredGridCells.Fixed(cellsCount),
            verticalItemSpacing = 5.dp,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            items(units) { unit ->
                ItemUnitDirectory(
                    currentName = unit.name,
                    currentShort = unit.short,
                    isEnabledUpdate = isEnableUpdate,
                    onDeleteClick = { onDeleteClick(unit.id) },
                    titleDelete = titleDelete,
                    onUpdateClick = { name, short ->
                        onUpdateClick(
                            unit.id,
                            name,
                            short
                        )
                    }
                )
            }
        }
    }
}
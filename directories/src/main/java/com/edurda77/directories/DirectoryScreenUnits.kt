package com.edurda77.directories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.edurda77.domain.model.newModels.MeasurementUnit


@Composable
fun DirectoryScreenUnits(
    modifier: Modifier = Modifier,
    isEnableUpdate: Boolean,
    units: List<MeasurementUnit>,
    cellsCount: Int,
    onDeleteClick: (String) -> Unit,
    onUpdateClick: (MeasurementUnit) -> Unit,
    titleDelete: String
) {
    if (units.isNotEmpty()) {
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
                    currentShort = unit.abbreviation,
                    isEnabledUpdate = isEnableUpdate,
                    onDeleteClick = { onDeleteClick(unit.id) },
                    titleDelete = titleDelete,
                    onUpdateClick = { name, short ->
                        onUpdateClick(
                            MeasurementUnit(
                                id = unit.id,
                                name = name,
                                abbreviation = short
                            )
                        )
                    }
                )
            }
        }
    }
}
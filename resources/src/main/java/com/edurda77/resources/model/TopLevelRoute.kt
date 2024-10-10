package com.edurda77.resources.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.edurda77.domain.model.NavigationRoute

data class TopLevelRoute(
    val name: String,
    val route: NavigationRoute,
    val icon: ImageVector
)
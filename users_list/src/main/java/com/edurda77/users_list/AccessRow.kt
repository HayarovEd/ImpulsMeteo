package com.edurda77.users_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.theme.Typography

@Composable
fun AccessRow(
    modifier: Modifier = Modifier,
    title: String,
    isAccess: Boolean,
    onClickAccess: () -> Unit
) {
    Row(
        modifier = modifier.clickable {
            onClickAccess()
        },
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Checkbox(
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary,
                uncheckedColor = MaterialTheme.colorScheme.outline,
                checkmarkColor = MaterialTheme.colorScheme.background
            ),
            checked = isAccess,
            onCheckedChange = { onClickAccess() }
        )
        Spacer(modifier = modifier.width(5.dp))
        Text(
            modifier = modifier,
            text = title,
            color = MaterialTheme.colorScheme.outline,
            style = Typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun AccessRowView1() {
    AccessRow(
        title = "Просмотр",
        isAccess = false,
        onClickAccess = {}
    )
}

@Preview
@Composable
private fun AccessRowView2() {
    AccessRow(
        title = "Просмотр",
        isAccess = true,
        onClickAccess = {}
    )
}
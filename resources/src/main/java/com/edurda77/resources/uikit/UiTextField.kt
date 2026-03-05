package com.edurda77.resources.uikit

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextFieldDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.edurda77.resources.theme.ImpulsMeteoTheme
import com.edurda77.resources.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiTextField(
    modifier: Modifier = Modifier,
    content: String,
    label: String,
    trailingIcon: ImageVector? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isOnlyDigit: Boolean = false,
    maxLines: Int = 1,
    onClickContent: (String) -> Unit,
    onClickTrailingIcon: () -> Unit = {},
) {
    val interactionSource = remember { MutableInteractionSource() }
    TextField(
        value = content,
        modifier = modifier
            .fillMaxWidth(),
        onValueChange = { text ->
            if (isOnlyDigit) {
                if (text.all { it.isDigit() }) {
                    onClickContent(text)
                }
            } else {
                onClickContent(text)
            }
        },
        enabled = enabled,
        readOnly = readOnly,
        textStyle = Typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onPrimaryContainer),
        // cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        label = {
            Text(
                text = label,
                style = Typography.labelSmall
            )
        },
        trailingIcon = {
            if (trailingIcon != null) {
                UiIconButton(
                    icon = trailingIcon,
                    onClick = onClickTrailingIcon,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
        colors = colors(
            focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(),
            unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(),
            cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
            focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
            unfocusedLabelColor = MaterialTheme.colorScheme.outline
        ),
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        /*decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                enabled = enabled,
                shape = MaterialTheme.shapes.medium,
                innerTextField = innerTextField,
                interactionSource = interactionSource,
                singleLine = true,
                value = content,
                visualTransformation = visualTransformation,
                label = {
                    Text(
                        text = label,
                        style = Typography.labelSmall
                    )
                },
                trailingIcon = {
                    if (trailingIcon != null) {
                        UiIconButton(
                            icon = trailingIcon,
                            onClick = onClickTrailingIcon,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                },
               // contentPadding = PaddingValues(vertical = 4.dp, horizontal = 10.dp),
                colors = colors(
                    focusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(),
                    unfocusedContainerColor = MaterialTheme.colorScheme.tertiary.copy(),
                    cursorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    focusedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unfocusedLabelColor = MaterialTheme.colorScheme.outline
                )
            )
        }*/
    )
}

@Preview(
    showBackground = true
)
@Composable
private fun UiTextFieldView() {
    ImpulsMeteoTheme {
        UiTextField(
            content = "",
            label = "hallo",
            onClickContent = {}
        )
    }
}

@Preview(
    showBackground = true
)
@Composable
private fun UiTextFieldView2() {
    ImpulsMeteoTheme {
        UiTextField(
            content = "world!",
            label = "hallo",
            onClickContent = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiTextFieldView3() {
    ImpulsMeteoTheme {
        UiTextField(
            content = "",
            label = "hallo",
            onClickContent = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun UiTextFieldView4() {
    ImpulsMeteoTheme {
        UiTextField(
            content = "world!",
            label = "hallo",
            onClickContent = {}
        )
    }
}
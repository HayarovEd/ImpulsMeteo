package com.edurda77.resources.uikit

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
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
    BasicTextField(
        value = content,
        modifier = modifier.fillMaxWidth(),
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
        textStyle = Typography.bodyLarge,
        // cursorBrush = SolidColor(colors.cursorColor(isError).value),
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        maxLines = maxLines,
        decorationBox = @Composable { innerTextField ->
            TextFieldDefaults.DecorationBox(
                enabled = enabled,
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
                            onClick = onClickTrailingIcon
                        )
                    }
                },
                contentPadding = PaddingValues(vertical = 4.dp, horizontal = 10.dp)
            )
        }
    )
    /*TextField(
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp),
        value = content,
        enabled = enabled,
        readOnly = readOnly,
        onValueChange = {
            onClickContent(it)
        },
        trailingIcon = {
            if (trailingIcon != null) {
                UiIconButton(
                    icon = trailingIcon,
                    onClick = onClickTrailingIcon
                )
            }
        },
        textStyle = Typography.bodyLarge,
        label = {
            Text(
                text = label,
                style = Typography.labelSmall
            )
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        maxLines = 1,
        visualTransformation = visualTransformation
    )*/
}
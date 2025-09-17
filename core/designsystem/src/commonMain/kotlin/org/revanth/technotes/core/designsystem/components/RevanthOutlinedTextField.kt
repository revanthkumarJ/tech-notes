package org.revanth.technotes.core.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import org.revanth.technotes.core.designsystem.icon.AppIcons

@Composable
fun RevanthOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    colors: TextFieldColors = colors(
        focusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
        unfocusedBorderColor = MaterialTheme.colorScheme.secondaryContainer,
        errorBorderColor = MaterialTheme.colorScheme.error,
    ),
    textStyle: TextStyle = LocalTextStyle.current,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    config: TextFieldConfig = TextFieldConfig(),
    onClickClearIcon: () -> Unit = { onValueChange("") },
) {
    val isFocused by interactionSource.collectIsFocusedAsState()
    val showIcon by rememberUpdatedState(value.isNotEmpty())

    OutlinedTextField(
        shape = shape,
        colors = colors,
        value = value,
        label = { Text(text = label) },
        onValueChange = onValueChange,
        textStyle = textStyle,
        modifier = modifier.fillMaxWidth(),
        enabled = config.enabled,
        readOnly = config.readOnly,
        visualTransformation = config.visualTransformation,
        keyboardOptions = config.keyboardOptions,
        keyboardActions = config.keyboardActions,
        interactionSource = interactionSource,
        singleLine = config.singleLine,
        maxLines = config.maxLines,
        minLines = config.minLines,
        leadingIcon = config.leadingIcon,
        isError = config.isError,
        trailingIcon = @Composable {
            AnimatedContent(
                targetState = config.showClearIcon && isFocused && showIcon,
            ) {
                if (it) {
                    ClearIconButton(
                        showClearIcon = true,
                        clearIcon = config.clearIcon,
                        onClickClearIcon = onClickClearIcon,
                    )
                } else {
                    config.trailingIcon?.invoke()
                }
            }
        },
        supportingText = config.errorText?.let {
            {
                Text(
                    modifier = Modifier.testTag("errorTag"),
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}

@Composable
private fun ClearIconButton(
    showClearIcon: Boolean,
    clearIcon: ImageVector,
    onClickClearIcon: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = showClearIcon,
        modifier = modifier,
    ) {
        IconButton(
            onClick = onClickClearIcon,
            modifier = Modifier.semantics {
                contentDescription = "clearIcon"
            },
        ) {
            Icon(
                imageVector = clearIcon,
                contentDescription = "trailingIcon",
            )
        }
    }
}

data class TextFieldConfig(
    val enabled: Boolean = true,
    val showClearIcon: Boolean = true,
    val readOnly: Boolean = false,
    val clearIcon: ImageVector = AppIcons.Close,
    val isError: Boolean = false,
    val errorText: String? = null,
    val visualTransformation: VisualTransformation = VisualTransformation.None,
    val keyboardActions: KeyboardActions = KeyboardActions.Default,
    val singleLine: Boolean = true,
    val maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    val minLines: Int = 1,
    val keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    val trailingIcon: @Composable (() -> Unit)? = null,
    val leadingIcon: @Composable (() -> Unit)? = null,
)
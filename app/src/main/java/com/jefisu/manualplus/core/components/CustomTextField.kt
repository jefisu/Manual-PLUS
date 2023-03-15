package com.jefisu.manualplus.core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.ui.theme.dark_placerholderColor
import com.jefisu.manualplus.core.ui.theme.light_placerholderColor
import com.jefisu.manualplus.core.ui.theme.spacing

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CustomTextField(
    text: String,
    onTextChange: (String) -> Unit,
    placeholderText: String,
    nameTextField: String,
    @DrawableRes iconView: Int,
    modifier: Modifier = Modifier,
    readOnly: Boolean = false,
    isPassword: Boolean = false,
    isPrimaryColorBackground: Boolean = false,
) {
    val imeIsVisible = WindowInsets.isImeVisible
    val focusManager = LocalFocusManager.current
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var hasFocus by rememberSaveable { mutableStateOf(false) }
    val color =
        if (isPrimaryColorBackground) MaterialTheme.colors.background else MaterialTheme.colors.onBackground
    val visualTransformation =
        if (isPassword && showPassword) VisualTransformation.None else PasswordVisualTransformation()

    LaunchedEffect(key1 = imeIsVisible) {
        if (!imeIsVisible) {
            hasFocus = false
            focusManager.clearFocus()
        }
    }

    LaunchedEffect(key1 = hasFocus) {
        if (readOnly && hasFocus) {
            focusManager.clearFocus()
        }
    }

    Box(modifier = modifier) {
        Text(
            text = nameTextField,
            fontSize = 8.sp,
            color = color,
            modifier = Modifier
                .offset(x = MaterialTheme.spacing.medium, y = (-6).dp)
                .background(if (isPrimaryColorBackground) MaterialTheme.colors.primary else MaterialTheme.colors.background)
                .padding(horizontal = MaterialTheme.spacing.extraSmall)
                .zIndex(1f)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            shape = RoundedCornerShape(8.dp),
            readOnly = readOnly,
            visualTransformation = visualTransformation,
            colors = TextFieldDefaults.textFieldColors(
                textColor = color,
                leadingIconColor = color,
                trailingIconColor = color,
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = color,
                focusedIndicatorColor = color,
                placeholderColor = when {
                    !isSystemInDarkTheme() && isPrimaryColorBackground -> dark_placerholderColor
                    isSystemInDarkTheme() && isPrimaryColorBackground -> light_placerholderColor
                    isSystemInDarkTheme() -> dark_placerholderColor
                    else -> light_placerholderColor
                }
            ),
            placeholder = {
                Text(
                    text = placeholderText,
                    fontSize = 12.sp
                )
            },
            leadingIcon = {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(iconView),
                        contentDescription = "Icon field",
                        modifier = Modifier.size(16.dp)
                    )
                    Box(
                        modifier = Modifier
                            .width(1.dp)
                            .height(16.dp)
                            .background(color)
                    )
                }
            },
            trailingIcon = {
                if (text.isNotBlank()) {
                    IconButton(onClick = {
                        if (isPassword) {
                            showPassword = !showPassword
                        } else onTextChange("")
                    }) {
                        Icon(
                            painter = painterResource(
                                when {
                                    isPassword -> R.drawable.ic_eye
                                    showPassword -> R.drawable.ic_eye_slash
                                    else -> R.drawable.ic_close_circle
                                }
                            ),
                            contentDescription = "Icon action"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .onFocusChanged { hasFocus = it.hasFocus }
        )
    }
}

@Preview
@Composable
fun PreviewCustomTextField() {
    var text by remember { mutableStateOf("") }

    ManualPLUSTheme {
        Surface {
            CustomTextField(
                text = text,
                onTextChange = { text = it },
                placeholderText = "john.doe@example.com",
                nameTextField = "Email",
                iconView = R.drawable.ic_email,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewPasswordTextField() {
    var text by remember { mutableStateOf("") }

    ManualPLUSTheme {
        Surface {
            CustomTextField(
                text = text,
                onTextChange = { text = it },
                nameTextField = "Password",
                isPassword = true,
                placeholderText = "Digit your password",
                iconView = R.drawable.ic_lock,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}
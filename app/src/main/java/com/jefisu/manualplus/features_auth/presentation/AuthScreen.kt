package com.jefisu.manualplus.features_auth.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.components.CustomButton
import com.jefisu.manualplus.core.components.CustomTextField
import com.jefisu.manualplus.core.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.ui.theme.spacing

@OptIn(ExperimentalMotionApi::class)
@Composable
fun AuthScreen() {
    var animateToEnd by rememberSaveable { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (animateToEnd) 1f else 0f,
        animationSpec = tween(700),
        label = ""
    )
    val context = LocalContext.current
    val motionScene = remember {
        context.resources
            .openRawResource(R.raw.auth_screen_motion_scene)
            .readBytes()
            .decodeToString()
    }

    MotionLayout(
        motionScene = MotionScene(content = motionScene),
        progress = progress,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .layoutId("logo")
                .size(150.dp)
        )
        Column(
            modifier = Modifier
                .layoutId("containerSignUp")
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colors.primary)
                .clickable {
                    if (animateToEnd) {
                        return@clickable
                    }
                    animateToEnd = true
                }
                .padding(top = 24.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(
                text = "Sign Up",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.background
            )
            Column(
                modifier = Modifier.alpha(if (animateToEnd) 1f else 0f)
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                CustomTextField(
                    text = "",
                    onTextChange = {},
                    placeholderText = "Jeferson Coutinho",
                    nameTextField = "Name",
                    iconView = R.drawable.ic_user,
                    isPrimaryColorBackground = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomTextField(
                    text = "",
                    onTextChange = {},
                    placeholderText = "jeferson.c@example.com",
                    nameTextField = "Email",
                    iconView = R.drawable.ic_email,
                    isPrimaryColorBackground = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomTextField(
                    text = "",
                    onTextChange = {},
                    placeholderText = "Digit your password",
                    nameTextField = "Password",
                    iconView = R.drawable.ic_lock,
                    isPrimaryColorBackground = true,
                    isPassword = true
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomButton(
                    text = "Create Account",
                    backgroundIsPrimary = true,
                    onClick = { }
                )
            }
        }
        Column(
            modifier = Modifier
                .layoutId("containerLogin")
                .clip(RoundedCornerShape(32.dp))
                .background(MaterialTheme.colors.background)
                .clickable {
                    if (!animateToEnd) {
                        return@clickable
                    }
                    animateToEnd = false
                }
                .padding(top = 24.dp, start = 32.dp, end = 32.dp)
        ) {
            Text(
                text = "Log In",
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.onBackground
            )
            if (!animateToEnd) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                CustomTextField(
                    text = "",
                    onTextChange = {},
                    placeholderText = "john.doe@example.com",
                    nameTextField = "Email",
                    iconView = R.drawable.ic_email,
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomTextField(
                    text = "",
                    onTextChange = {},
                    nameTextField = "Password",
                    isPassword = true,
                    placeholderText = "Digit your password",
                    iconView = R.drawable.ic_lock
                )
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Forgot Password?",
                    fontSize = 12.sp,
                    color = MaterialTheme.colors.onBackground,
                    modifier = Modifier.align(Alignment.End)
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomButton(
                    text = "Log In",
                    onClick = { }
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.5.dp)
                            .background(MaterialTheme.colors.onBackground)
                    )
                    Text(
                        text = "or",
                        style = MaterialTheme.typography.body1,
                        color = MaterialTheme.colors.onBackground,
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.5.dp)
                            .background(MaterialTheme.colors.onBackground)
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                CustomButton(
                    text = "Sign In with Google",
                    icon = R.drawable.google_logo,
                    onClick = { }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewAuthScreen() {
    ManualPLUSTheme {
        Surface {
            AuthScreen()
        }
    }
}
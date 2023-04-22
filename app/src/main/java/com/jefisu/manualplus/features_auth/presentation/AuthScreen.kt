package com.jefisu.manualplus.features_auth.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.navigation.NavController
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.BottomSheet
import com.jefisu.manualplus.core.presentation.components.CustomButton
import com.jefisu.manualplus.core.presentation.components.CustomTextField
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.destinations.HomeScreenDestination
import com.jefisu.manualplus.features_auth.presentation.components.GoogleButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@RootNavGraph(start = true)
@Destination
@Composable
fun AuthScreen(
    navController: NavController,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)

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

    LaunchedEffect(state.error, sheetState, state.canNavigate) {
        if (state.error != null) {
            sheetState.show()
        }
        if (!sheetState.isVisible) {
            onEvent(AuthEvent.ErrorDisplayed)
        }
        if (state.canNavigate) {
            navController.backQueue.clear()
            navController.navigate(HomeScreenDestination)
        }
    }

    BottomSheet(
        error = state.error?.asString().orEmpty(),
        sheetState = sheetState,
        onOkClick = { onEvent(AuthEvent.ErrorDisplayed) }
    ) {
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
            BodyClickable(
                clickEnabled = progress == 0f,
                backgroundColor = MaterialTheme.colors.primary,
                onClick = { animateToEnd = !animateToEnd },
                modifier = Modifier.layoutId("containerSignUp"),
                content = {
                    Text(
                        text = stringResource(R.string.sign_up),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.background
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    CustomTextField(
                        text = state.emailSignUp,
                        onTextChange = { onEvent(AuthEvent.EnterEmail(it, true)) },
                        placeholderText = "john.doe@example.com",
                        nameTextField = "Email",
                        iconView = R.drawable.ic_email,
                        isPrimaryColorBackground = true,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                        readOnly = progress == 0f
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomTextField(
                        text = state.passwordSignUp,
                        onTextChange = { onEvent(AuthEvent.EnterPassword(it, true)) },
                        placeholderText = stringResource(R.string.digit_your_password),
                        nameTextField = stringResource(R.string.password),
                        iconView = R.drawable.ic_lock,
                        isPrimaryColorBackground = true,
                        isPassword = true,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                        readOnly = progress == 0f
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomTextField(
                        text = state.repeatPasswordSignUp,
                        onTextChange = { onEvent(AuthEvent.EnterRepeatPassword(it)) },
                        placeholderText = stringResource(R.string.repeat_your_password),
                        nameTextField = stringResource(R.string.repeat_password),
                        iconView = R.drawable.ic_lock,
                        isPrimaryColorBackground = true,
                        isPassword = true,
                        imeAction = ImeAction.Done,
                        keyboardAction = focusManager::clearFocus,
                        readOnly = progress == 0f
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomButton(
                        text = stringResource(R.string.create_account),
                        backgroundIsPrimary = true,
                        onClick = { onEvent(AuthEvent.SignUp) },
                        isLoading = state.isLoading && state.showLoadingButton == AuthViewModel.LoadingButton.SignUp
                    )
                }
            )
            BodyClickable(
                clickEnabled = progress == 1f,
                backgroundColor = MaterialTheme.colors.background,
                onClick = { animateToEnd = !animateToEnd },
                modifier = Modifier.layoutId("containerLogin"),
                content = {
                    Text(
                        text = stringResource(R.string.log_in),
                        style = MaterialTheme.typography.h6,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground
                    )
                    if (!animateToEnd) {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                        CustomTextField(
                            text = state.emailSignIn,
                            onTextChange = { onEvent(AuthEvent.EnterEmail(it, false)) },
                            placeholderText = "john.doe@example.com",
                            nameTextField = "Email",
                            iconView = R.drawable.ic_email,
                            imeAction = ImeAction.Next,
                            keyboardAction = { focusManager.moveFocus(FocusDirection.Down) }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        CustomTextField(
                            text = state.passwordSignIn,
                            onTextChange = { onEvent(AuthEvent.EnterPassword(it, false)) },
                            nameTextField = stringResource(R.string.password),
                            isPassword = true,
                            placeholderText = stringResource(R.string.digit_your_password),
                            iconView = R.drawable.ic_lock,
                            imeAction = ImeAction.Done,
                            keyboardAction = focusManager::clearFocus
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = stringResource(R.string.forgot_password),
                            fontSize = 12.sp,
                            color = MaterialTheme.colors.onBackground,
                            modifier = Modifier.align(Alignment.End)
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        CustomButton(
                            text = stringResource(R.string.log_in),
                            isLoading = state.isLoading && state.showLoadingButton == AuthViewModel.LoadingButton.NormalLogin,
                            onClick = { onEvent(AuthEvent.LoginEmail) }
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
                                text = stringResource(R.string.or),
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
                        GoogleButton(
                            onResult = { onEvent(AuthEvent.LoginGoogle(it)) },
                            isLoading = state.isLoading && state.showLoadingButton == AuthViewModel.LoadingButton.Google
                        )
                    }
                }
            )
        }
    }
}

@Composable
private fun BodyClickable(
    clickEnabled: Boolean,
    backgroundColor: Color,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(backgroundColor)
            .then(modifier)
    ) {
        Column(
            content = content,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 24.dp,
                    start = 32.dp,
                    end = 32.dp
                )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(if (clickEnabled) 1f else 0f)
                .height(if (clickEnabled) 80.dp else 0.dp)
                .clickable(onClick = onClick)
        )
    }
}
package com.jefisu.manualplus.features_auth.presentation

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalComposeUiApi::class
)
@RootNavGraph(start = true)
@Destination
@Composable
fun AuthScreen(
    navController: NavController,
    state: AuthState,
    onEvent: (AuthEvent) -> Unit
) {
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 50
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var isSigning by rememberSaveable { mutableStateOf(true) }
    val percentOffsetY by animateFloatAsState(
        label = "",
        targetValue = when {
            !isSigning -> 0.74f
            !isSigning && isKeyboardVisible -> 1f
            else -> 0f
        },
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    LaunchedEffect(state.error, sheetState, state.canNavigate, isSigning) {
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
        if (isKeyboardVisible) {
            focusManager.clearFocus()
        }
    }

    BottomSheet(
        error = state.error?.asString().orEmpty(),
        sheetState = sheetState,
        onOkClick = { onEvent(AuthEvent.ErrorDisplayed) }
    ) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .padding(vertical = MaterialTheme.spacing.large)
                .size(150.dp)
                .align(Alignment.CenterHorizontally)
        )
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            BodyClickable(
                titleContainer = stringResource(R.string.sign_up),
                textColor = MaterialTheme.colors.background,
                clickEnabled = isSigning,
                backgroundColor = MaterialTheme.colors.primary,
                onClick = { isSigning = !isSigning },
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.large),
                content = {
                    CustomTextField(
                        text = state.emailSignUp,
                        onTextChange = { onEvent(AuthEvent.EnterEmail(it, true)) },
                        placeholderText = "john.doe@example.com",
                        nameTextField = "Email",
                        iconView = R.drawable.ic_email,
                        isPrimaryColorBackground = true,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                        readOnly = isSigning
                    )
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
                        readOnly = isSigning
                    )
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
                        readOnly = isSigning
                    )
                    CustomButton(
                        text = stringResource(R.string.create_account),
                        backgroundIsPrimary = true,
                        onClick = { onEvent(AuthEvent.SignUp) },
                        isLoading = state.isLoading && state.showLoadingButton == AuthViewModel.LoadingButton.SignUp
                    )
                }
            )
            BodyClickable(
                titleContainer = stringResource(R.string.log_in),
                textColor = MaterialTheme.colors.onBackground,
                clickEnabled = !isSigning,
                backgroundColor = MaterialTheme.colors.background,
                onClick = { isSigning = !isSigning },
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = maxHeight * percentOffsetY)
                    .padding(top = MaterialTheme.spacing.extraLarge),
                content = {
                    CustomTextField(
                        text = state.emailSignIn,
                        onTextChange = { onEvent(AuthEvent.EnterEmail(it, false)) },
                        placeholderText = "john.doe@example.com",
                        nameTextField = "Email",
                        iconView = R.drawable.ic_email,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) }
                    )
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
                    Text(
                        text = stringResource(R.string.forgot_password),
                        fontSize = 12.sp,
                        color = MaterialTheme.colors.onBackground,
                        modifier = Modifier.align(Alignment.End)
                    )
                    CustomButton(
                        text = stringResource(R.string.log_in),
                        isLoading = state.isLoading && state.showLoadingButton == AuthViewModel.LoadingButton.NormalLogin,
                        onClick = { onEvent(AuthEvent.LoginEmail) }
                    )
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
                    GoogleButton(
                        onResult = { onEvent(AuthEvent.LoginGoogle(it)) },
                        isLoading = state.isLoading && state.showLoadingButton == AuthViewModel.LoadingButton.Google
                    )
                }
            )
        }
    }
}

@Composable
private fun BodyClickable(
    titleContainer: String,
    textColor: Color,
    clickEnabled: Boolean,
    backgroundColor: Color,
    onClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top
) {
    Surface(
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = backgroundColor,
        modifier = modifier.fillMaxSize()
    ) {
        Box {
            Column(
                verticalArrangement = verticalArrangement,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = MaterialTheme.spacing.large,
                        vertical = 24.dp
                    )
            ) {
                Text(
                    text = titleContainer,
                    style = MaterialTheme.typography.h6,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                content()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth(if (clickEnabled) 1f else 0f)
                    .height(if (clickEnabled) 80.dp else 0.dp)
                    .clickable(onClick = onClick)
            )
        }
    }
}
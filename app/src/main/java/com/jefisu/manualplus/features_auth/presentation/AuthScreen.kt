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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import androidx.hilt.navigation.compose.hiltViewModel
import com.jefisu.manualplus.R
import com.jefisu.manualplus.core.presentation.components.CustomButton
import com.jefisu.manualplus.core.presentation.components.CustomTextField
import com.jefisu.manualplus.core.presentation.ui.theme.ManualPLUSTheme
import com.jefisu.manualplus.core.presentation.ui.theme.spacing
import com.jefisu.manualplus.features_auth.presentation.components.GoogleButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMotionApi::class, ExperimentalMaterialApi::class)
@Composable
fun AuthScreen(
    navigateToHome: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel()
) {
    val loginState by viewModel.loginState.collectAsState()
    val signUpState by viewModel.signUpState.collectAsState()
    val error by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
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

    LaunchedEffect(error, sheetState, viewModel.navigateEvent) {
        if (error != null) {
            sheetState.show()
        }
        if (!sheetState.isVisible) {
            viewModel.errorDisplayed()
        }
        viewModel.navigateEvent.collect { canNavigate ->
            if (canNavigate) {
                navigateToHome()
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetElevation = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scrimColor = Color.Black.copy(0.5f),
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp)
                    .padding(MaterialTheme.spacing.medium)
            ) {
                Text(
                    text = error?.asString().orEmpty(),
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                CustomButton(
                    text = "OK",
                    buttonShape = RoundedCornerShape(8.dp),
                    onClick = {
                        viewModel.errorDisplayed()
                        scope.launch { sheetState.hide() }
                    }
                )
            }
        }
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
            Column(
                modifier = Modifier
                    .layoutId("containerSignUp")
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colors.primary)
                    .clickable(enabled = !isLoading) {
                        if (!animateToEnd) {
                            animateToEnd = true
                        }
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
                        text = signUpState.email,
                        onTextChange = { viewModel.enteredEmail(it, isSigning = true) },
                        placeholderText = "jeferson.c@example.com",
                        nameTextField = "Email",
                        iconView = R.drawable.ic_email,
                        isPrimaryColorBackground = true,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomTextField(
                        text = signUpState.password,
                        onTextChange = { viewModel.enteredPassword(it, isSigning = true) },
                        placeholderText = "Digit your password",
                        nameTextField = "Password",
                        iconView = R.drawable.ic_lock,
                        isPrimaryColorBackground = true,
                        isPassword = true,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomTextField(
                        text = signUpState.repeatPassword,
                        onTextChange = viewModel::enteredName,
                        placeholderText = "Repeat your password",
                        nameTextField = "Repeat password",
                        iconView = R.drawable.ic_lock,
                        isPrimaryColorBackground = true,
                        isPassword = true,
                        imeAction = ImeAction.Done,
                        keyboardAction = focusManager::clearFocus,
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomButton(
                        text = "Create Account",
                        backgroundIsPrimary = true,
                        onClick = viewModel::signUp
                    )
                }
            }
            Column(
                modifier = Modifier
                    .layoutId("containerLogin")
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(MaterialTheme.colors.background)
                    .clickable(enabled = !isLoading) {
                        if (animateToEnd) {
                            animateToEnd = false
                        }
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
                        text = loginState.email,
                        onTextChange = { viewModel.enteredEmail(it, isSigning = false) },
                        placeholderText = "john.doe@example.com",
                        nameTextField = "Email",
                        iconView = R.drawable.ic_email,
                        imeAction = ImeAction.Next,
                        keyboardAction = { focusManager.moveFocus(FocusDirection.Down) },
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    CustomTextField(
                        text = loginState.password,
                        onTextChange = { viewModel.enteredPassword(it, isSigning = false) },
                        nameTextField = "Password",
                        isPassword = true,
                        placeholderText = "Digit your password",
                        iconView = R.drawable.ic_lock,
                        imeAction = ImeAction.Done,
                        keyboardAction = focusManager::clearFocus,
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
                        isLoading = isLoading,
                        onClick = viewModel::login
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
                    GoogleButton(
                        onResult = viewModel::loginWithGoogle,
                        isLoading = isLoading && loginState.email.isBlank() && loginState.password.isBlank()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewAuthScreen() {
    ManualPLUSTheme {
        Surface {
            AuthScreen(navigateToHome = { })
        }
    }
}